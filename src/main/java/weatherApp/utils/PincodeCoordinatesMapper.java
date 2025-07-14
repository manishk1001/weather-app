package weatherApp.utils;

import weatherApp.dto.PincodeCoordinatesResponse;
import weatherApp.entity.PincodeCoordinates;

public class PincodeCoordinatesMapper {
    public static PincodeCoordinates mapToEntity(PincodeCoordinatesResponse pincodeCoordinatesResponse){
        PincodeCoordinates pincodeCoordinates = new PincodeCoordinates();
        pincodeCoordinates.setName(pincodeCoordinatesResponse.getName());
        pincodeCoordinates.setPincode(pincodeCoordinatesResponse.getPincode());
        pincodeCoordinates.setLatitude(pincodeCoordinatesResponse.getLat());
        pincodeCoordinates.setLongitude(pincodeCoordinatesResponse.getLon());
        pincodeCoordinates.setCountry(pincodeCoordinatesResponse.getCountry());
        return pincodeCoordinates;
    }
}
