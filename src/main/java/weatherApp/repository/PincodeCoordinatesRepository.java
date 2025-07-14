package weatherApp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import weatherApp.entity.PincodeCoordinates;

import java.util.Optional;

public interface PincodeCoordinatesRepository extends JpaRepository<PincodeCoordinates, Long> {
    Optional<PincodeCoordinates> findByPincode(String pincode);
}
