package weatherApp.dto;

import lombok.Builder;
import lombok.Data;
import weatherApp.entity.PincodeCoordinates;

import java.time.LocalDate;
@Data
@Builder
public class WeatherInfoResponse {
    private PincodeCoordinatesDto pincode;
    private LocalDate date;
    private Double temperature;
    private Double temperatureMin;
    private Double temperatureMax;
    private Double feelsLike;
    private Integer pressure;
    private Integer humidity;
    private String weatherMain;
    private String weatherDescription;
    private Double windSpeed;
}
