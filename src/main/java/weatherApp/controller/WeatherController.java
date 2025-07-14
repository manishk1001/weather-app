package weatherApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import weatherApp.dto.WeatherInfoRequest;
import weatherApp.dto.WeatherInfoResponse;
import weatherApp.response.RestApiResponse;
import weatherApp.service.WeatherInfoService;
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/weather")
@Tag(name = "Weather", description = "Get weather by pincode and date")
public class WeatherController {
    private final WeatherInfoService weatherInfoService;
    @Operation(
            summary = "Get weather info by pincode and date",
            description = "Returns current or cached weather data"
    )
    @GetMapping
    public ResponseEntity<RestApiResponse<WeatherInfoResponse>> getWeather(
            @Valid @ModelAttribute WeatherInfoRequest weatherInfoRequest) {
        return ResponseEntity.ok(RestApiResponse.<WeatherInfoResponse>builder()
                .data(weatherInfoService.getWeatherInfo(weatherInfoRequest.getPincode(), weatherInfoRequest.getDate())).build());
    }
}
