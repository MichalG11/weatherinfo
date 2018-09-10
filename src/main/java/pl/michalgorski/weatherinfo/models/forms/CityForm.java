package pl.michalgorski.weatherinfo.models.forms;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class CityForm {

    @Pattern(regexp = "[A-ZĘÓĄŚŁŻŹĆŃa-zęóąśłżźćń]{3,21}", message = "Nazwa miasta od 3 do 21 liter!")
    private String city;

    @Pattern(regexp = "[A-Za-z][A-Za-z]", message = "Podaj dwie litery!")
    private String countryCode;

}
