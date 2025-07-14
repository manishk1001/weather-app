package weatherApp.utils;

import weatherApp.dto.OpenWeatherResponse;
import weatherApp.dto.PincodeCoordinatesDto;
import weatherApp.dto.WeatherInfoResponse;
import weatherApp.entity.PincodeCoordinates;
import weatherApp.entity.WeatherInfo;

import java.time.LocalDate;

public class WeatherInfoMapper {
    public static WeatherInfoResponse toDto(WeatherInfo weatherInfo){
        PincodeCoordinatesDto pincodeCoordinatesDto = new PincodeCoordinatesDto();

        pincodeCoordinatesDto.setPincode(weatherInfo.getPincode().getPincode());
        pincodeCoordinatesDto.setLatitude(weatherInfo.getPincode().getLatitude());
        pincodeCoordinatesDto.setLongitude(weatherInfo.getPincode().getLongitude());
        pincodeCoordinatesDto.setName(weatherInfo.getPincode().getName());
        pincodeCoordinatesDto.setCountry(weatherInfo.getPincode().getCountry());

        return WeatherInfoResponse.builder()
                .pincode(pincodeCoordinatesDto)
                .date(weatherInfo.getDate())
                .feelsLike(weatherInfo.getFeelsLike())
                .humidity(weatherInfo.getHumidity())
                .pressure(weatherInfo.getPressure())
                .temperature(weatherInfo.getTemperature())
                .weatherMain(weatherInfo.getWeatherMain())
                .weatherDescription(weatherInfo.getWeatherDescription())
                .windSpeed(weatherInfo.getWindSpeed())
                .temperatureMax(weatherInfo.getTemperatureMax())
                .temperatureMin(weatherInfo.getTemperatureMin())
                .build();
    }
    public static  WeatherInfo toEntity(OpenWeatherResponse openWeatherResponse, PincodeCoordinates pincodeCoordinates, LocalDate date){
        WeatherInfo info = new WeatherInfo();
        info.setPincode(pincodeCoordinates);
        info.setDate(date);
        info.setTemperature(openWeatherResponse.getMain().getTemp());
        info.setFeelsLike(openWeatherResponse.getMain().getFeelsLike());
        info.setPressure(openWeatherResponse.getMain().getPressure());
        info.setHumidity(openWeatherResponse.getMain().getHumidity());
        info.setWeatherMain(openWeatherResponse.getWeather().get(0).getMain());
        info.setWeatherDescription(openWeatherResponse.getWeather().get(0).getDescription());
        info.setTemperatureMax(openWeatherResponse.getMain().getTempMax());
        info.setTemperatureMin(openWeatherResponse.getMain().getTempMin());
        info.setWindSpeed(openWeatherResponse.getWind().getSpeed());
        return info;
    }
}
