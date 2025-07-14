package weatherApp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import weatherApp.client.OpenWeatherClient;
import weatherApp.dto.OpenWeatherResponse;
import weatherApp.entity.PincodeCoordinates;
import weatherApp.entity.WeatherInfo;
import weatherApp.repository.WeatherInfoRepository;
import weatherApp.utils.WeatherInfoMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeatherInfoServiceTest {

    @Mock
    private PincodeCoordinatesService pincodeCoordinatesService;

    @Mock
    private WeatherInfoRepository weatherInfoRepository;

    @Mock
    private OpenWeatherClient openWeatherClient;

    @InjectMocks
    private WeatherInfoService weatherInfoService;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnWeatherFromDatabase() {
        String pincode = "123456";
        LocalDate date = LocalDate.now();

        PincodeCoordinates coord = new PincodeCoordinates();
        coord.setId(1L);
        coord.setPincode(pincode);
        coord.setLatitude(28.6);
        coord.setLongitude(77.2);

        WeatherInfo weatherInfo = new WeatherInfo();
        weatherInfo.setPincode(coord);
        weatherInfo.setDate(date);

        when(pincodeCoordinatesService.getPincodeCoordinates(pincode)).thenReturn(coord);
        when(weatherInfoRepository.findByPincodeAndDate(coord, date)).thenReturn(Optional.of(weatherInfo));

        var result = weatherInfoService.getWeatherInfo(pincode, date);

        assertNotNull(result);
        assertEquals(pincode, result.getPincode().getPincode());
        verify(weatherInfoRepository, never()).save(any());
    }

    @Test
    void shouldFetchFromApiIfNotInDb() {
        String pincode = "123456";
        LocalDate date = LocalDate.now();

        PincodeCoordinates coord = new PincodeCoordinates();
        coord.setId(1L);
        coord.setPincode(pincode);
        coord.setLatitude(28.6);
        coord.setLongitude(77.2);

        // Build mock OpenWeatherResponse with dummy values
        OpenWeatherResponse.Main main = new OpenWeatherResponse.Main();
        main.setTemp(300.0);
        main.setFeelsLike(299.0);
        main.setTempMin(298.0);
        main.setTempMax(302.0);
        main.setPressure(1012);
        main.setHumidity(85);

        OpenWeatherResponse.Weather weather = new OpenWeatherResponse.Weather();
        weather.setMain("Rain");
        weather.setDescription("moderate rain");

        OpenWeatherResponse.Wind wind = new OpenWeatherResponse.Wind();
        wind.setSpeed(5.5);

        OpenWeatherResponse apiResponse = new OpenWeatherResponse();
        apiResponse.setMain(main);
        apiResponse.setWeather(List.of(weather));
        apiResponse.setWind(wind);
        apiResponse.setDt(123456789L);

        WeatherInfo mappedWeatherInfo = WeatherInfoMapper.toEntity(apiResponse, coord, date);

        when(pincodeCoordinatesService.getPincodeCoordinates(pincode)).thenReturn(coord);
        when(weatherInfoRepository.findByPincodeAndDate(coord, date)).thenReturn(Optional.empty());
        when(openWeatherClient.getWeatherByCoordinates(coord.getLatitude(), coord.getLongitude())).thenReturn(apiResponse);
        when(weatherInfoRepository.save(any())).thenReturn(mappedWeatherInfo);

        var result = weatherInfoService.getWeatherInfo(pincode, date);

        assertNotNull(result);
        assertEquals(pincode, result.getPincode().getPincode());
        verify(weatherInfoRepository).save(any());
    }

    @Test
    void shouldThrowExceptionWhenClientFails() {
        String pincode = "123456";
        LocalDate date = LocalDate.now();

        PincodeCoordinates coord = new PincodeCoordinates();
        coord.setId(1L);
        coord.setPincode(pincode);
        coord.setLatitude(28.6);
        coord.setLongitude(77.2);

        when(pincodeCoordinatesService.getPincodeCoordinates(pincode)).thenReturn(coord);
        when(weatherInfoRepository.findByPincodeAndDate(coord, date)).thenReturn(Optional.empty());
        when(openWeatherClient.getWeatherByCoordinates(anyDouble(), anyDouble()))
                .thenThrow(new RuntimeException("External API failure"));

        assertThrows(RuntimeException.class, () -> weatherInfoService.getWeatherInfo(pincode, date));
        verify(weatherInfoRepository, never()).save(any());
    }
}
