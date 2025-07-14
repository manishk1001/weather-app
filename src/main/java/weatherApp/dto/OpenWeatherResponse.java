package weatherApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class OpenWeatherResponse {
    private List<Weather> weather;
    private Main main;
    private Wind wind;
    private long dt;
    @Data
    public static class Weather {
        private String main;
        private String description;
    }
    @Data
    public static class Main {
        private double temp;

        @JsonProperty("feels_like")
        private double feelsLike;

        @JsonProperty("temp_min")
        private double tempMin;

        @JsonProperty("temp_max")
        private double tempMax;

        private int pressure;
        private int humidity;
    }
    @Data
    public static class Wind {
        private double speed;
    }
}
