package pl.michalgorski.weatherinfo.models;

import lombok.Data;

@Data
public class Config {

    public static final String URL_TO_API = "http://api.openweathermap.org/data/2.5/weather?q=";

    public static final String URL_TO_API_AIR = "http://api.openweathermap.org/pollution/v1/co/";
    public static final String URL_TO_API_AIR2 = "/current.json?appid=";

    public static final String URL_TO_API_FUTURE = "http://api.openweathermap.org/data/2.5/forecast?q=";

    public static final String API_KEY = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

}
