package pl.michalgorski.weatherinfo.models.dto;

import lombok.Data;

import java.util.List;

@Data
public class WeatherFutureParameter {
    private String city;
    private double lat;
    private double lon;
    private List<FutureDataForOneDay> listOfFutureData;

    private boolean ok;
}
