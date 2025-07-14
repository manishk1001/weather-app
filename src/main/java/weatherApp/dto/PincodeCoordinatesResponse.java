package weatherApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PincodeCoordinatesResponse {

    @JsonProperty("zip")
    private String pincode;
    private String name;
    private double lat;
    private double lon;
    private String country;
}

