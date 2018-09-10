package pl.michalgorski.weatherinfo.controllers;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.michalgorski.weatherinfo.models.dto.WeatherCurrentParameter;
import pl.michalgorski.weatherinfo.models.dto.WeatherFutureParameter;
import pl.michalgorski.weatherinfo.models.forms.CityForm;
import pl.michalgorski.weatherinfo.models.services.DownloadWeatherService;

import javax.validation.Valid;

@Controller
public class MainController {

    final DownloadWeatherService downloadWeatherService;

    @Autowired
    public MainController(DownloadWeatherService downloadWeatherService) {
        this.downloadWeatherService = downloadWeatherService;
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("cityForm", new CityForm());
        return "index";
    }

    @PostMapping("/findCurrent")
    public String findCurrentWeather(@ModelAttribute("cityForm") @Valid CityForm cityForm, BindingResult bindingResult,
                              Model model) throws JSONException {
        if(bindingResult.hasErrors()) {
            model.addAttribute("infoAboutErrors1", "Niepoprawne dane!");
            return "index";
        }
        WeatherCurrentParameter weatherCurrentParameter = downloadWeatherService.getCurrentWeather(cityForm.getCity(),
                cityForm.getCountryCode());

        if(!weatherCurrentParameter.isOk()) {
            model.addAttribute("infoAboutErrors1", "Nie ma takiego miasta!");
            return "index";
        }
        model.addAttribute("weatherData", weatherCurrentParameter);
        return "currentWeather";
    }

    @PostMapping("/findFuture")
    public String findFutureWeather(@ModelAttribute("cityForm") @Valid CityForm cityForm, BindingResult bindingResult,
                                     Model model) throws JSONException {
        if(bindingResult.hasErrors()) {
            model.addAttribute("infoAboutErrors2", "Niepoprawne dane!");
            return "currentWeather";
        }
        WeatherFutureParameter weatherFutureParameter = downloadWeatherService.getFutureWeather(cityForm.getCity(),
                cityForm.getCountryCode());

        if(!weatherFutureParameter.isOk()) {
            model.addAttribute("infoAboutErrors2", "Nie ma takiego miasta!");
            return "currentWeather";
        }
        model.addAttribute("weatherData", weatherFutureParameter);
        return "futureWeather";
    }
}