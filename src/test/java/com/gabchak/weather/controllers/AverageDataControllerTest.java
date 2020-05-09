package com.gabchak.weather.controllers;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.gabchak.weather.constants.TestConstants;
import com.gabchak.weather.constants.TestStaticModels;
import com.gabchak.weather.config.WebMvcConfiguration;
import com.gabchak.weather.dto.WeatherDataDto;
import com.gabchak.weather.services.AverageDataService;
import com.gabchak.weather.services.security.jwt.JwtTokenProvider;
import java.util.Collections;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AverageDataController.class)
@WithMockUser(roles = "PAID_USER")
class AverageDataControllerTest {

  private static final String CRUD_PATH =
      WebMvcConfiguration.API_PREFIX + AverageDataController.CRUD_PATH;

  private static final String INVALID_STRING = "INVALID STRING";

  @Autowired
  private AverageDataController controller;
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private AverageDataService service;
  @MockBean
  private JwtTokenProvider provider;
  @MockBean
  private AuthenticationManager authenticationManager;

  @Test
  void findAllBetweenDates_Ok() throws Exception {
    List<WeatherDataDto> serviceResponse = Collections.singletonList(TestStaticModels.WEATHER_DATA_DTO);
    Mockito.when(service
        .findAllBetweenDates(Mockito.any(), Mockito.any()))
        .thenReturn(serviceResponse);


    mockMvc.perform(get(CRUD_PATH)
        .param(WeatherDataController.DATE_FROM, TestConstants.DATE_MONTH_AGO)
        .param(WeatherDataController.DATE_TO, TestConstants.DATE_NOW))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.[0].airQuality", is(TestStaticModels.WEATHER_DATA_DTO.getAirQuality())))
        .andExpect(jsonPath("$.[0].date", Matchers.is(TestConstants.DATE_NOW)))
        .andExpect(jsonPath("$.[0].humidity", is(TestStaticModels.WEATHER_DATA_DTO.getHumidity())))
        .andExpect(jsonPath("$.[0].pressure", is(TestStaticModels.WEATHER_DATA_DTO.getPressure())))
        .andExpect(jsonPath("$.[0].temperature", is(TestStaticModels.WEATHER_DATA_DTO.getTemperature())))
        .andExpect(jsonPath("$.[0].windSpeed", is(TestStaticModels.WEATHER_DATA_DTO.getWindSpeed())));
  }

  @Test
  void findAllBetweenDates_NotFound() throws Exception {
    List<WeatherDataDto> emptyServiceResponse = Collections.emptyList();
    Mockito.when(service
        .findAllBetweenDates(Mockito.any(), Mockito.any()))
        .thenReturn(emptyServiceResponse);
    mockMvc.perform(get(CRUD_PATH)
        .param(WeatherDataController.DATE_FROM, TestConstants.DATE_MONTH_AGO)
        .param(WeatherDataController.DATE_TO, TestConstants.DATE_NOW))
        .andExpect(status().isOk())
        .andExpect(content().string("[]"));
  }

  @Test
  void findAllBetweenDates_BadRequest() throws Exception {
    mockMvc.perform(get(CRUD_PATH)
        .param(WeatherDataController.DATE_FROM, INVALID_STRING)
        .param(WeatherDataController.DATE_TO, INVALID_STRING))
        .andExpect(status().isBadRequest());
  }
}