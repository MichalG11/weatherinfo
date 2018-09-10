package pl.michalgorski.weatherinfo.models.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FutureDataForOneDay {
    private long nightTemperature;
    private long nightPressure;
    private long dayTemperature;
    private long dayPressure;
    private LocalDateTime localDateTime;

    public FutureDataForOneDay(long nightTemperature, long nightPressure, long dayTemperature, long dayPressure,
                               LocalDateTime localDateTime) {
        this.nightTemperature = nightTemperature;
        this.nightPressure = nightPressure;
        this.dayTemperature = dayTemperature;
        this.dayPressure = dayPressure;
        this.localDateTime = localDateTime;
    }
}
