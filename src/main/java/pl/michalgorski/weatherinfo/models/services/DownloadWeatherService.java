package pl.michalgorski.weatherinfo.models.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import pl.michalgorski.weatherinfo.models.Config;
import pl.michalgorski.weatherinfo.models.dto.FutureDataForOneDay;
import pl.michalgorski.weatherinfo.models.dto.WeatherCurrentParameter;
import pl.michalgorski.weatherinfo.models.dto.WeatherFutureParameter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DownloadWeatherService {

    private static DownloadWeatherService INSTANCE = new DownloadWeatherService();

    private DownloadWeatherService() {
    }

    public static DownloadWeatherService getINSTANCE() {
        return INSTANCE;
    }

    public WeatherCurrentParameter getCurrentWeather(String cityName, String countryCod) throws JSONException {
        WeatherCurrentParameter weatherCurrentParameter = new WeatherCurrentParameter();
        weatherCurrentParameter.setOk(true);

        String url = Config.URL_TO_API + cityName + "," + countryCod + "&appid=" + Config.API_KEY;
        Optional<String> cleanJson = readWebsite(url);
        if(!cleanJson.isPresent()){
            weatherCurrentParameter.setOk(false);
            return weatherCurrentParameter;
        }

        JSONObject root = new JSONObject(cleanJson.get());
        JSONObject main = root.getJSONObject("main");
        JSONObject sys = root.getJSONObject("sys");
        JSONObject coord = root.getJSONObject("coord");

        weatherCurrentParameter.setCity(root.getString("name"));
        weatherCurrentParameter.setTemperature(Math.round(main.getDouble("temp") - 273));
        weatherCurrentParameter.setCountryCode(sys.getString("country"));
        weatherCurrentParameter.setPressure(Math.round(main.getDouble("pressure")));
        weatherCurrentParameter.setVisibility(Math.round(root.getDouble("visibility")));
        weatherCurrentParameter.setLon(coord.getDouble("lon"));
        weatherCurrentParameter.setLat(coord.getDouble("lat"));

        String url2 = Config.URL_TO_API_AIR + (int)weatherCurrentParameter.getLat() + "," +
                (int)weatherCurrentParameter.getLon() + Config.URL_TO_API_AIR2 + Config.API_KEY;

        Optional<String> cleanJson2 = readWebsite(url2);
        if(!cleanJson2.isPresent()){
            weatherCurrentParameter.setOk(false);
            return weatherCurrentParameter;
        }

        JSONObject root2 = new JSONObject(cleanJson2.get());
        JSONArray list2 = root2.getJSONArray("data");
        JSONObject objectFromJSONArray2 = list2.getJSONObject(0);

        weatherCurrentParameter.setAirPollution(objectFromJSONArray2.getDouble("value"));

        return weatherCurrentParameter;
    }

    public WeatherFutureParameter getFutureWeather(String cityName, String countryCod) throws JSONException {
        WeatherFutureParameter weatherFutureParameter = new WeatherFutureParameter();
        weatherFutureParameter.setOk(true);

        String url3 = Config.URL_TO_API_FUTURE + cityName + "," + countryCod + "&appid=" + Config.API_KEY;

        Optional<String> cleanJson3 = readWebsite(url3);
        if(!cleanJson3.isPresent()){
            weatherFutureParameter.setOk(false);
            return weatherFutureParameter;
        }

        JSONObject root3 = new JSONObject(cleanJson3.get());
        JSONArray list = root3.getJSONArray("list");
        JSONObject city = root3.getJSONObject("city");
        JSONObject coord = city.getJSONObject("coord");

        weatherFutureParameter.setCity(city.getString("name"));
        weatherFutureParameter.setLat(coord.getDouble("lat"));
        weatherFutureParameter.setLon(coord.getDouble("lon"));

        int[] dataTimeTable = new int[list.length()];
        double[] tempTable = new double[list.length()];
        double[] pressureTable = new double[list.length()];
        JSONObject objectFromJSONArray;
        JSONObject main3;

        for (int i = 0; i < list.length(); i++) {
            objectFromJSONArray = list.getJSONObject(i);
            main3 = objectFromJSONArray.getJSONObject("main");
            dataTimeTable[i] = LocalDateTime.ofEpochSecond(objectFromJSONArray.getInt("dt"),
                                                                        0,ZoneOffset.UTC).getDayOfYear();
            tempTable[i] = main3.getDouble("temp");
            pressureTable[i] = main3.getDouble("pressure");
        }

        int index = 0;

        for (int i = 0; i < list.length(); i++) {
            if(dataTimeTable[0] != dataTimeTable[i]) {
                index = i;
                break;
            }
        }

        double[] sumTempNight = new double[4];
        double[] sumPressureNight = new double[4];
        double[] sumTempDay = new double[4];
        double[] sumPressureDay = new double[4];

        List<FutureDataForOneDay> listOfFutureData = new ArrayList<>();

        for (int i1 = 0; i1 < 4; i1++) {

            for (int i = index + (8 * i1); i < index + 8*(i1 + 1) - 5; i++) {
                sumTempNight[i1] += tempTable[i];
                sumPressureNight[i1] += pressureTable[i];
            }
            for (int i = index + 4 + (8 * i1); i < index + 8*(i1 + 1) - 1; i++) {
                sumTempDay[i1] += tempTable[i];
                sumPressureDay[i1] += pressureTable[i];
            }

            listOfFutureData.add(new FutureDataForOneDay(
                    Math.round(sumTempNight[i1]/3)-273, Math.round(sumPressureNight[i1]/3),
                    Math.round(sumTempDay[i1]/3)-273, Math.round(sumPressureDay[i1]/3),
                LocalDateTime.ofEpochSecond(list.getJSONObject(index+(i1*8)).getInt("dt"), 0, ZoneOffset.UTC)));
        }

        weatherFutureParameter.setListOfFutureData(listOfFutureData);

        return weatherFutureParameter;
    }

    private Optional<String> readWebsite(String url){
        StringBuilder stringBuilder = new StringBuilder();

        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();

            InputStream inputStream = httpURLConnection.getInputStream();

            int response = 0;

            while ((response = inputStream.read()) != -1) {

                stringBuilder.append((char) response);
            }
        }catch (IOException e){
            return Optional.empty();
        }
        return Optional.of(stringBuilder.toString());
    }

}
