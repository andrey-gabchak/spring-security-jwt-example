package com.gabchak.weather.controllers;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.gabchak.weather.constants.TestConstants;
import com.gabchak.weather.constants.TestStaticModels;
import com.gabchak.weather.config.WebMvcConfiguration;
import com.gabchak.weather.dto.WeatherDataDto;
import com.gabchak.weather.services.WeatherDataService;
import com.gabchak.weather.services.security.jwt.JwtTokenProvider;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import javax.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = WeatherDataController.class)
@WithMockUser(roles = "ADMIN")
class WeatherDataControllerTest {

  private static final String WEATHER_CRUD_PATH =
      WebMvcConfiguration.API_PREFIX + WeatherDataController.CRUD_PATH;

  private static final String WEATHER_FORECASTING =
      WebMvcConfiguration.API_PREFIX
          + WeatherDataController.CRUD_PATH
          + WeatherDataController.LAST_ONE_FOR_TODAY;

  private static final String INVALID_STRING = "INVALID STRING";

  @Autowired
  private WeatherDataController controller;
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private WeatherDataService service;
  @MockBean
  private JwtTokenProvider provider;
  @MockBean
  private AuthenticationManager authenticationManager;

  @Test
  void insert_statusOk() throws Exception {
    String validJson = TestStaticModels.JSON_BUILDER.build().excludeId().jsonInt();
    Mockito.when(service.save(Mockito.any())).thenReturn(TestStaticModels.WEATHER_DATA_DTO);
    mockMvc.perform(
        post(WEATHER_CRUD_PATH).contentType(MediaType.APPLICATION_JSON)
            .content(validJson))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.airQuality", is(TestStaticModels.WEATHER_DATA_DTO.getAirQuality())))
        .andExpect(jsonPath("$.date", is(LocalDate.now().toString())))
        .andExpect(jsonPath("$.humidity", is(TestStaticModels.WEATHER_DATA_DTO.getHumidity())))
        .andExpect(jsonPath("$.pressure", is(TestStaticModels.WEATHER_DATA_DTO.getPressure())))
        .andExpect(jsonPath("$.temperature", is(TestStaticModels.WEATHER_DATA_DTO.getTemperature())))
        .andExpect(jsonPath("$.windSpeed", is(TestStaticModels.WEATHER_DATA_DTO.getWindSpeed())));
  }

  @Test
  void findAllBetweenDates_Ok() throws Exception {
    Page<WeatherDataDto> serviceResponse = new PageImpl<>(Collections.singletonList(TestStaticModels.WEATHER_DATA_DTO));
    Mockito.when(service
        .findAllBetweenDates(Mockito.any(), Mockito.any(),
            Mockito.any(), Mockito.any()))
        .thenReturn(serviceResponse);

    mockMvc.perform(get(WEATHER_CRUD_PATH)
        .param(WeatherDataController.DATE_FROM, TestConstants.DATE_MONTH_AGO)
        .param(WeatherDataController.DATE_TO, TestConstants.DATE_NOW))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content.[0].airQuality", is(TestStaticModels.WEATHER_DATA_DTO.getAirQuality())))
        .andExpect(jsonPath("$.content.[0].date", Matchers.is(TestConstants.DATE_NOW)))
        .andExpect(jsonPath("$.content.[0].humidity", is(TestStaticModels.WEATHER_DATA_DTO.getHumidity())))
        .andExpect(jsonPath("$.content.[0].pressure", is(TestStaticModels.WEATHER_DATA_DTO.getPressure())))
        .andExpect(jsonPath("$.content.[0].temperature", is(TestStaticModels.WEATHER_DATA_DTO.getTemperature())))
        .andExpect(jsonPath("$.content.[0].windSpeed", is(TestStaticModels.WEATHER_DATA_DTO.getWindSpeed())));
  }

  @Test
  void findAllBetweenDates_NotFound() throws Exception {
    Page<WeatherDataDto> emptyServiceResponse = new PageImpl<>(Collections.emptyList());
    Mockito.when(service
        .findAllBetweenDates(Mockito.any(), Mockito.any(),
            Mockito.any(), Mockito.any()))
        .thenReturn(emptyServiceResponse);
    mockMvc.perform(get(WEATHER_CRUD_PATH)
        .param(WeatherDataController.DATE_FROM, TestConstants.DATE_MONTH_AGO)
        .param(WeatherDataController.DATE_TO, TestConstants.DATE_NOW))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content", Matchers.empty()));
  }

  @Test
  void findAllBetweenDates_BadRequest() throws Exception {
    mockMvc.perform(get(WEATHER_CRUD_PATH)
        .param(WeatherDataController.DATE_FROM, INVALID_STRING)
        .param(WeatherDataController.DATE_TO, INVALID_STRING))
        .andExpect(status().isBadRequest());
  }

  @Test
  void findLastOneForToday_Ok() throws Exception {

    Mockito.when(service
        .findLastOneForToday())
        .thenReturn(Optional.of(TestStaticModels.WEATHER_DATA_DTO));

    mockMvc.perform(get(WEATHER_FORECASTING)
        .param(WeatherDataController.DATE_FROM, TestConstants.DATE_MONTH_AGO)
        .param(WeatherDataController.DATE_TO, TestConstants.DATE_NOW))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.airQuality", is(TestStaticModels.WEATHER_DATA_DTO.getAirQuality())))
        .andExpect(jsonPath("$.date", Matchers.is(TestConstants.DATE_NOW)))
        .andExpect(jsonPath("$.humidity", is(TestStaticModels.WEATHER_DATA_DTO.getHumidity())))
        .andExpect(jsonPath("$.pressure", is(TestStaticModels.WEATHER_DATA_DTO.getPressure())))
        .andExpect(jsonPath("$.temperature", is(TestStaticModels.WEATHER_DATA_DTO.getTemperature())))
        .andExpect(jsonPath("$.windSpeed", is(TestStaticModels.WEATHER_DATA_DTO.getWindSpeed())));
  }

  @Test
  void findLastOneForToday_NotFound() throws Exception {
    Mockito.when(service
        .findLastOneForToday())
        .thenReturn(Optional.empty());

    mockMvc.perform(get(WEATHER_FORECASTING)
        .param(WeatherDataController.DATE_FROM, TestConstants.DATE_MONTH_AGO)
        .param(WeatherDataController.DATE_TO, TestConstants.DATE_NOW))
        .andExpect(status().isNoContent());
  }

  @Test
  void update_Ok() throws Exception {
    String validJson = TestStaticModels.JSON_BUILDER.build().jsonInt();
    Mockito.when(service.save(TestStaticModels.WEATHER_DATA_DTO))
        .thenReturn(TestStaticModels.WEATHER_DATA_DTO);
    mockMvc.perform(put(WEATHER_CRUD_PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .content(validJson))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(TestStaticModels.WEATHER_DATA_DTO.getId())))
        .andExpect(jsonPath("$.airQuality", is(TestStaticModels.WEATHER_DATA_DTO.getAirQuality())))
        .andExpect(jsonPath("$.date", Matchers.is(TestConstants.DATE_NOW)))
        .andExpect(jsonPath("$.humidity", is(TestStaticModels.WEATHER_DATA_DTO.getHumidity())))
        .andExpect(jsonPath("$.pressure", is(TestStaticModels.WEATHER_DATA_DTO.getPressure())))
        .andExpect(jsonPath("$.temperature", is(TestStaticModels.WEATHER_DATA_DTO.getTemperature())))
        .andExpect(jsonPath("$.windSpeed", is(TestStaticModels.WEATHER_DATA_DTO.getWindSpeed())));
  }

  @Test
  void findById_Ok() throws Exception {
    Mockito.when(service.findById(1))
        .thenReturn(Optional.of(TestStaticModels.WEATHER_DATA_DTO));
    mockMvc.perform(get(WEATHER_CRUD_PATH + "/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.airQuality", is(TestStaticModels.WEATHER_DATA_DTO.getAirQuality())))
        .andExpect(jsonPath("$.date", Matchers.is(TestConstants.DATE_NOW)))
        .andExpect(jsonPath("$.humidity", is(TestStaticModels.WEATHER_DATA_DTO.getHumidity())))
        .andExpect(jsonPath("$.pressure", is(TestStaticModels.WEATHER_DATA_DTO.getPressure())))
        .andExpect(jsonPath("$.temperature", is(TestStaticModels.WEATHER_DATA_DTO.getTemperature())))
        .andExpect(jsonPath("$.windSpeed", is(TestStaticModels.WEATHER_DATA_DTO.getWindSpeed())));
  }

  @Test
  void findById_NotFound() throws Exception {
    Mockito.when(service.findById(1))
        .thenReturn(Optional.empty());
    mockMvc.perform(get(WEATHER_CRUD_PATH + "/1"))
        .andExpect(status().isNoContent());
  }

  @Test
  public void deleteById() throws Exception {
    ArgumentCaptor<Integer> valueCapture = ArgumentCaptor.forClass(Integer.class);
    doNothing().when(service).deleteById(valueCapture.capture());
    mockMvc.perform(delete(WEATHER_CRUD_PATH + "/1"))
        .andExpect(status().isOk());
    verify(service, times(1)).deleteById(1);
    assertEquals(1, valueCapture.getValue());
  }

  @Test
  void validationInsert_IdExists() throws Exception {
    String idExistsJson = TestStaticModels.JSON_BUILDER.build().jsonInt();
    mockMvc.perform(
        post(WEATHER_CRUD_PATH).contentType(MediaType.APPLICATION_JSON)
            .content(idExistsJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void validationInsert_WrongDate() throws Exception {
    String invalidDateJson = TestStaticModels.JSON_BUILDER.build()
        .excludeId().setDate("mistake").jsonInt();
    mockMvc.perform(
        post(WEATHER_CRUD_PATH).contentType(MediaType.APPLICATION_JSON)
            .content(invalidDateJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void validationInsert_WrongTemperature() throws Exception {
    String invalidTemperatureJson = TestStaticModels.JSON_BUILDER.build()
        .excludeId().setTemperature(200.0).jsonInt();
    mockMvc.perform(
        post(WEATHER_CRUD_PATH).contentType(MediaType.APPLICATION_JSON)
            .content(invalidTemperatureJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void validationInsert_WrongPressure() throws Exception {
    String invalidPressureJson = TestStaticModels.JSON_BUILDER.build()
        .excludeId().setPressure(-1.0).jsonInt();
    mockMvc.perform(
        post(WEATHER_CRUD_PATH).contentType(MediaType.APPLICATION_JSON)
            .content(invalidPressureJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void validationInsert_WrongWindSpeed() throws Exception {
    String invalidWindJson = TestStaticModels.JSON_BUILDER.build()
        .excludeId().setWindSpeed(-1.0).jsonInt();
    mockMvc.perform(
        post(WEATHER_CRUD_PATH).contentType(MediaType.APPLICATION_JSON)
            .content(invalidWindJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void validationInsert_WrongAirQuality() throws Exception {
    String invalidAirJson = TestStaticModels.JSON_BUILDER.build()
        .excludeId().setAirQuality(-1.0).jsonInt();
    mockMvc.perform(
        post(WEATHER_CRUD_PATH).contentType(MediaType.APPLICATION_JSON)
            .content(invalidAirJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void validationInsert_WrongHumidity() throws Exception {
    String invalidAirJson = TestStaticModels.JSON_BUILDER.build()
        .excludeId().setHumidity(-1.0).jsonInt();
    mockMvc.perform(
        post(WEATHER_CRUD_PATH).contentType(MediaType.APPLICATION_JSON)
            .content(invalidAirJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void validationUpdate_MissedId() throws Exception {
    String missedIdJson = TestStaticModels.JSON_BUILDER.build().excludeId().jsonInt();
    Mockito.when(service.save(TestStaticModels.WEATHER_DATA_DTO))
        .thenReturn(TestStaticModels.WEATHER_DATA_DTO);
    mockMvc.perform(put(WEATHER_CRUD_PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .content(missedIdJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void validationUpdate_WrongDate() throws Exception {
    String invalidDateJson = TestStaticModels.JSON_BUILDER.build()
        .excludeId().setDate("mistake").jsonInt();
    mockMvc.perform(
        put(WEATHER_CRUD_PATH).contentType(MediaType.APPLICATION_JSON)
            .content(invalidDateJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void validationUpdate_WrongTemperature() throws Exception {
    String invalidTemperatureJson = TestStaticModels.JSON_BUILDER.build()
        .excludeId().setTemperature(200.0).jsonInt();
    mockMvc.perform(
        put(WEATHER_CRUD_PATH).contentType(MediaType.APPLICATION_JSON)
            .content(invalidTemperatureJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void validationUpdate_WrongPressure() throws Exception {
    String invalidPressureJson = TestStaticModels.JSON_BUILDER.build()
        .excludeId().setPressure(-1.0).jsonInt();
    mockMvc.perform(
        put(WEATHER_CRUD_PATH).contentType(MediaType.APPLICATION_JSON)
            .content(invalidPressureJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void validationUpdate_WrongWindSpeed() throws Exception {
    String invalidWindJson = TestStaticModels.JSON_BUILDER.build()
        .excludeId().setWindSpeed(-1.0).jsonInt();
    mockMvc.perform(
        put(WEATHER_CRUD_PATH).contentType(MediaType.APPLICATION_JSON)
            .content(invalidWindJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void validationUpdate_WrongAirQuality() throws Exception {
    String invalidAirJson = TestStaticModels.JSON_BUILDER.build()
        .excludeId().setAirQuality(-1.0).jsonInt();
    mockMvc.perform(
        put(WEATHER_CRUD_PATH).contentType(MediaType.APPLICATION_JSON)
            .content(invalidAirJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void validationUpdate_WrongHumidity() throws Exception {
    String invalidAirJson = TestStaticModels.JSON_BUILDER.build()
        .excludeId().setHumidity(-1.0).jsonInt();
    mockMvc.perform(
        put(WEATHER_CRUD_PATH).contentType(MediaType.APPLICATION_JSON)
            .content(invalidAirJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  void validationDeleteById_WrongId() {
    Assertions.assertThatThrownBy(() -> mockMvc.perform(delete(WEATHER_CRUD_PATH + "/-1"))
        .andExpect(status().is5xxServerError()))
        .hasCause(new ConstraintViolationException("deleteById.id: must be greater than or equal to 1",
            new HashSet<>()));
  }

  @Test
  void validationFindById_WrongId() {
    Assertions.assertThatThrownBy(() -> mockMvc.perform(get(WEATHER_CRUD_PATH + "/-1"))
        .andExpect(status().is5xxServerError()))
        .hasCause(new ConstraintViolationException("findById.id: must be greater than or equal to 1",
            new HashSet<>()));
  }
}
