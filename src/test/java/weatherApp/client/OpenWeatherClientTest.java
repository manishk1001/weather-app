package weatherApp.client;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;
import weatherApp.dto.OpenWeatherResponse;
import weatherApp.dto.PincodeCoordinatesResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = {
        "openweather.api.key=dummy-api-key",
        "openweather.weather.url=http://mock-weather.com",
        "openweather.geo.url=http://mock-geo.com"
})
class OpenWeatherClientTest {

    @Autowired
    private OpenWeatherClient client;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void shouldFetchCoordinatesSuccessfully() {
        // Arrange
        String pincode = "123456";
        PincodeCoordinatesResponse mockResponse = new PincodeCoordinatesResponse();
        mockResponse.setLat(28.6);
        mockResponse.setLon(77.2);
        mockResponse.setPincode(pincode);
        mockResponse.setCountry("IN");
        mockResponse.setName("MockCity");

        ResponseEntity<PincodeCoordinatesResponse> response =
                new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), eq(PincodeCoordinatesResponse.class)))
                .thenReturn(response);

        // Act
        PincodeCoordinatesResponse result = client.getCoordinatesByPincode(pincode);

        // Assert
        assertNotNull(result);
        assertEquals(28.6, result.getLat());
        assertEquals("123456", result.getPincode());
    }

    @Test
    void shouldFetchWeatherSuccessfully() {
        // Arrange
        OpenWeatherResponse mockResponse = new OpenWeatherResponse();
        mockResponse.setDt(1726660758L);

        ResponseEntity<OpenWeatherResponse> response =
                new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), eq(OpenWeatherResponse.class)))
                .thenReturn(response);

        // Act
        OpenWeatherResponse result = client.getWeatherByCoordinates(28.6, 77.2);

        // Assert
        assertNotNull(result);
        assertEquals(1726660758L, result.getDt());
    }

    @Test
    void shouldThrowExceptionIfCoordinatesFetchFails() {
        ResponseEntity<PincodeCoordinatesResponse> response =
                new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

        when(restTemplate.getForEntity(anyString(), eq(PincodeCoordinatesResponse.class)))
                .thenReturn(response);

        assertThrows(RuntimeException.class, () -> client.getCoordinatesByPincode("000000"));
    }

    @Test
    void shouldThrowExceptionIfWeatherFetchFails() {
        ResponseEntity<OpenWeatherResponse> response =
                new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        when(restTemplate.getForEntity(anyString(), eq(OpenWeatherResponse.class)))
                .thenReturn(response);

        assertThrows(RuntimeException.class, () -> client.getWeatherByCoordinates(0.0, 0.0));
    }
}
