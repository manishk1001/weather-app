package weatherApp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import weatherApp.client.OpenWeatherClient;
import weatherApp.dto.OpenWeatherResponse;
import weatherApp.dto.WeatherInfoResponse;
import weatherApp.entity.PincodeCoordinates;
import weatherApp.entity.WeatherInfo;
import weatherApp.exception.ApiException;
import weatherApp.repository.WeatherInfoRepository;
import weatherApp.utils.WeatherInfoMapper;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherInfoService {

    private final PincodeCoordinatesService pincodeCoordinatesService;
    private final WeatherInfoRepository weatherInfoRepository;
    private final OpenWeatherClient openWeatherClient;

    @Cacheable(value = "weatherCache", key = "#pincode + '_' + #localDate.toString()")
    public WeatherInfoResponse getWeatherInfo(String pincode, LocalDate localDate) {
        log.info("Request to get weather info for pincode: {}, date: {}", pincode, localDate);

        PincodeCoordinates coordinates = pincodeCoordinatesService.getPincodeCoordinates(pincode);

        WeatherInfo weatherInfo = weatherInfoRepository.findByPincodeAndDate(coordinates, localDate)
                .orElseGet(() -> fetchAndSaveWeather(coordinates, localDate));

        log.info("Weather info fetched successfully for pincode: {}, date: {}", pincode, localDate);

        return WeatherInfoMapper.toDto(weatherInfo);
    }

    private WeatherInfo fetchAndSaveWeather(PincodeCoordinates coordinates, LocalDate date) {
        log.info("Fetching weather from OpenWeather API for lat={}, lon={}, date={}",
                coordinates.getLatitude(), coordinates.getLongitude(), date);
        try {
            OpenWeatherResponse response = openWeatherClient.getWeatherByCoordinates(
                    coordinates.getLatitude(), coordinates.getLongitude());

            WeatherInfo entity = WeatherInfoMapper.toEntity(response, coordinates, date);
            WeatherInfo saved = weatherInfoRepository.save(entity);
            log.info("Saved weather info for pincode: {}, date: {}", coordinates.getPincode(), date);
            return saved;

        } catch (ApiException ex) {
            log.error("Failed to fetch weather data: {}", ex.getMessage());
            throw new ApiException("Unable to retrieve weather data", "WEATHER_FETCH_FAILED", 503);
        }
    }
}
