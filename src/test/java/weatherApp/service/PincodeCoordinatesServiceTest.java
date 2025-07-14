package weatherApp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import weatherApp.client.OpenWeatherClient;
import weatherApp.dto.PincodeCoordinatesResponse;
import weatherApp.entity.PincodeCoordinates;
import weatherApp.repository.PincodeCoordinatesRepository;
import weatherApp.utils.PincodeCoordinatesMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PincodeCoordinatesServiceTest {

    @Mock
    private PincodeCoordinatesRepository repository;

    @Mock
    private OpenWeatherClient client;

    @InjectMocks
    private PincodeCoordinatesService service;

    private AutoCloseable closeable;

    @BeforeEach
    void init() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnCoordinatesFromDatabase() {
        String pincode = "123456";

        PincodeCoordinates coords = new PincodeCoordinates();
        coords.setId(1L);
        coords.setPincode(pincode);
        coords.setLatitude(28.6);
        coords.setLongitude(77.2);

        when(repository.findByPincode(pincode)).thenReturn(Optional.of(coords));

        PincodeCoordinates result = service.getPincodeCoordinates(pincode);

        assertNotNull(result);
        assertEquals(pincode, result.getPincode());
        verify(client, never()).getCoordinatesByPincode(any());
    }

    @Test
    void shouldFetchAndSaveCoordinatesIfNotInDb() {
        String pincode = "123456";

        when(repository.findByPincode(pincode)).thenReturn(Optional.empty());

        PincodeCoordinatesResponse response = new PincodeCoordinatesResponse();
        response.setLat(28.6);
        response.setLon(77.2);
        response.setPincode(pincode);
        response.setCountry("IN");
        response.setName("Delhi");

        PincodeCoordinates mapped = PincodeCoordinatesMapper.mapToEntity(response);
        mapped.setId(1L);

        when(client.getCoordinatesByPincode(pincode)).thenReturn(response);
        when(repository.save(any())).thenReturn(mapped);

        PincodeCoordinates result = service.getPincodeCoordinates(pincode);

        assertNotNull(result);
        assertEquals(pincode, result.getPincode());
        verify(repository).save(any());
    }
}
