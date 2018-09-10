package pl.michalgorski.weatherinfo.models.dto;

import lombok.Data;

@Data
public class WeatherCurrentParameter {
    private long temperature;
    private String city;
    private String countryCode;
    private long pressure;
    private long visibility;
    private double lat;
    private double lon;
    private double airPollution;

    private boolean ok;
}


