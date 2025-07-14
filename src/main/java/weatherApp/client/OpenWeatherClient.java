package weatherApp.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import weatherApp.dto.OpenWeatherResponse;
import weatherApp.dto.PincodeCoordinatesResponse;
import weatherApp.exception.ApiException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenWeatherClient {

    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${openweather.weather.url}")
    private String weatherUrl;

    @Value("${openweather.geo.url}")
    private String geoUrl;

    private final RestTemplate restTemplate;

    public PincodeCoordinatesResponse getCoordinatesByPincode(String pincode) {
        String url = UriComponentsBuilder.fromHttpUrl(geoUrl)
                .queryParam("zip", pincode + ",IN")
                .queryParam("appid", apiKey)
                .toUriString();

        log.info("Calling OpenWeather Geo API for pincode: {}", pincode);

        try {
            ResponseEntity<PincodeCoordinatesResponse> response =
                    restTemplate.getForEntity(url, PincodeCoordinatesResponse.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                log.warn("Geo API returned error for pincode {}: {}", pincode, response.getStatusCode());
                throw new ApiException("Failed to fetch coordinates", "GEO_API_FAILURE", 502);
            }

            log.info("Successfully fetched coordinates for pincode: {}", pincode);
            return response.getBody();
        } catch (Exception ex) {
            log.error("Exception occurred while fetching coordinates for pincode {}: {}", pincode, ex.getMessage());
            throw new ApiException("Geo API unreachable", "GEO_API_UNAVAILABLE", 503);
        }
    }

    public OpenWeatherResponse getWeatherByCoordinates(double lat, double lon) {
        String url = UriComponentsBuilder.fromHttpUrl(weatherUrl)
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("appid", apiKey)
                .toUriString();

        log.info("Calling OpenWeather Weather API for lat={}, lon={}", lat, lon);

        try {
            ResponseEntity<OpenWeatherResponse> response =
                    restTemplate.getForEntity(url, OpenWeatherResponse.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                log.warn("Weather API error for lat={}, lon={}: {}", lat, lon, response.getStatusCode());
                throw new ApiException("Failed to fetch weather data", "WEATHER_API_FAILURE", 502);
            }

            log.info("Successfully fetched weather for lat={}, lon={}", lat, lon);
            return response.getBody();
        } catch (Exception ex) {
            log.error("Exception occurred while fetching weather for lat={}, lon={}: {}", lat, lon, ex.getMessage());
            throw new ApiException("Weather API unreachable", "WEATHER_API_UNAVAILABLE", 503);
        }
    }
}
