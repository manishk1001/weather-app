package weatherApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "weather_info", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"pincode_id", "weather_date"})
})
public class WeatherInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pincode_id", nullable = false)
    private PincodeCoordinates pincode;

    @Column(name = "weather_date", nullable = false)
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