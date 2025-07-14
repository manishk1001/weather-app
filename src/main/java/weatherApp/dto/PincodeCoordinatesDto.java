package weatherApp.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PincodeCoordinatesDto {
    private String pincode;
    private Double latitude;
    private Double longitude;
    private String name;
    private String country;
}
