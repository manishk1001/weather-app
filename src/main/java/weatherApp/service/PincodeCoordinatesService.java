package weatherApp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import weatherApp.client.OpenWeatherClient;
import weatherApp.dto.PincodeCoordinatesResponse;
import weatherApp.entity.PincodeCoordinates;
import weatherApp.exception.ApiException;
import weatherApp.repository.PincodeCoordinatesRepository;
import weatherApp.utils.PincodeCoordinatesMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class PincodeCoordinatesService {

    private final PincodeCoordinatesRepository pincodeCoordinatesRepository;
    private final OpenWeatherClient openWeatherClient;

    @Cacheable(value = "coordinatesCache", key = "#pincode")
    public PincodeCoordinates getPincodeCoordinates(String pincode) {
        log.info("Getting coordinates for pincode: {}", pincode);
        return pincodeCoordinatesRepository.findByPincode(pincode)
                .orElseGet(() -> fetchPincodeCoordinates(pincode));
    }

    private PincodeCoordinates fetchPincodeCoordinates(String pincode) {
        log.info("Fetching coordinates from OpenWeather API for pincode: {}", pincode);
        try {
            PincodeCoordinatesResponse response = openWeatherClient.getCoordinatesByPincode(pincode);
            PincodeCoordinates entity = PincodeCoordinatesMapper.mapToEntity(response);
            PincodeCoordinates saved = pincodeCoordinatesRepository.save(entity);
            log.info("Saved new coordinates for pincode: {}", pincode);
            return saved;
        } catch (ApiException ex) {
            log.warn("Failed to fetch coordinates from external API for pincode {}: {}", pincode, ex.getMessage());
            throw new ApiException("Unable to fetch location for pincode " + pincode, "PINCODE_LOOKUP_FAILED", 503);
        }
    }
}
