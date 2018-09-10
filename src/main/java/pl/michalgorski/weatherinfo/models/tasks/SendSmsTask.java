package pl.michalgorski.weatherinfo.models.tasks;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.michalgorski.weatherinfo.models.services.DownloadWeatherService;
import pl.michalgorski.weatherinfo.models.services.SmsService;

@Service
public class SendSmsTask {

    final DownloadWeatherService downloadWeatherService;
    final SmsService smsService;

    @Autowired
    public SendSmsTask(DownloadWeatherService downloadWeatherService, SmsService smsService) {
        this.downloadWeatherService = downloadWeatherService;
        this.smsService = smsService;
    }

    @Scheduled(cron = "0 0 6,20 * * *")
    public void runSmsTask() throws JSONException {
        double currentVisibility;
        currentVisibility = downloadWeatherService.getCurrentWeather("krakow", "pl").getVisibility();

        if(currentVisibility < 500) {
            smsService.sendSms("606290906", "Uwaga! Widocznosc w Krakowie ponizej 500m!");
        }
    }

}
