package com.gabchak.example.constants;

import static com.gabchak.example.constants.TestConstants.DEFAULT_NUMBER_ITEMS_PER_PAGE;


import com.gabchak.example.dto.AverageDataDto;
import com.gabchak.example.dto.WeatherDataDto;
import com.gabchak.example.dto.enums.Roles;
import com.gabchak.example.models.AverageData;
import com.gabchak.example.models.Role;
import com.gabchak.example.models.User;
import com.gabchak.example.models.WeatherData;
import com.gabchak.example.services.security.jwt.JwtUser;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestStaticModels {

  public static final WeatherData WEATHER_DATA;
  public static final WeatherDataDto WEATHER_DATA_DTO;
  public static final AverageData AVERAGE_DATA;
  public static final AverageDataDto AVERAGE_DATA_DTO;
  public static DataJson.DataJsonBuilder JSON_BUILDER;
  public static final JwtUser JWT_USER;
  public static final List<Role> ROLES = Arrays.asList(
      Role.fromEnum(Roles.ADMIN),
      Role.fromEnum(Roles.PAID_USER),
      Role.fromEnum(Roles.FREE_USER));
  public static final List<SimpleGrantedAuthority> AUTHORITIES =
      ROLES.stream()
      .map(role ->
          new SimpleGrantedAuthority("ROLE_" + role.getName()))
      .collect(Collectors.toList());

  public static final User USER;


  static {
    WEATHER_DATA_DTO = new WeatherDataDto();
    WEATHER_DATA_DTO.setId(1);
    WEATHER_DATA_DTO.setAirQuality(75);
    WEATHER_DATA_DTO.setDate(LocalDate.now());
    WEATHER_DATA_DTO.setHumidity(25);
    WEATHER_DATA_DTO.setTemperature(20);
    WEATHER_DATA_DTO.setPressure(777);
    WEATHER_DATA_DTO.setWindSpeed(DEFAULT_NUMBER_ITEMS_PER_PAGE);

    WEATHER_DATA = new WeatherData();
    WEATHER_DATA.setId(1);
    WEATHER_DATA.setAirQuality(75);
    WEATHER_DATA.setDate(LocalDate.now());
    WEATHER_DATA.setHumidity(25);
    WEATHER_DATA.setTemperature(20);
    WEATHER_DATA.setPressure(777);
    WEATHER_DATA.setWindSpeed(DEFAULT_NUMBER_ITEMS_PER_PAGE);

    AVERAGE_DATA = new AverageData();
    AVERAGE_DATA.setId(1);
    AVERAGE_DATA.setAirQuality(75.0);
    AVERAGE_DATA.setHumidity(25.0);
    AVERAGE_DATA.setTemperature(20.0);
    AVERAGE_DATA.setPressure(777.0);
    AVERAGE_DATA.setWindSpeed(10.0);
    AVERAGE_DATA.setDate(LocalDate.now());

    AVERAGE_DATA_DTO = new AverageDataDto();
    AVERAGE_DATA_DTO.setId(1);
    AVERAGE_DATA_DTO.setAirQuality(75.0);
    AVERAGE_DATA_DTO.setHumidity(25.0);
    AVERAGE_DATA_DTO.setTemperature(20.0);
    AVERAGE_DATA_DTO.setPressure(777.0);
    AVERAGE_DATA_DTO.setWindSpeed(10.0);
    AVERAGE_DATA_DTO.setDate(LocalDate.now());

    JSON_BUILDER = DataJson.builder()
        .id(WEATHER_DATA_DTO.getId())
        .airQuality(WEATHER_DATA_DTO.getAirQuality().doubleValue())
        .humidity(WEATHER_DATA_DTO.getHumidity().doubleValue())
        .pressure(WEATHER_DATA_DTO.getPressure().doubleValue())
        .temperature(WEATHER_DATA_DTO.getTemperature().doubleValue())
        .windSpeed(WEATHER_DATA_DTO.getWindSpeed().doubleValue())
        .date(LocalDate.now().toString());

    JWT_USER = new JwtUser(1,
        "admin@gmail.com",
        "firstName",
        "lastName",
        "password",
        AUTHORITIES);

    USER = new User();
    USER.setEmail("admin@gmail.com");
    USER.setPassword("password");
    USER.setFirstName("firstName");
    USER.setLastName("lastName");
    USER.setId(1);
    USER.setRoles(ROLES);
  }
}
