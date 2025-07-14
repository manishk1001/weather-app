package weatherApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import weatherApp.entity.PincodeCoordinates;
import weatherApp.entity.WeatherInfo;

import java.time.LocalDate;
import java.util.Optional;

public interface WeatherInfoRepository extends JpaRepository<WeatherInfo, Long> {
    Optional<WeatherInfo> findByPincodeAndDate(PincodeCoordinates pincode, LocalDate date);
}
