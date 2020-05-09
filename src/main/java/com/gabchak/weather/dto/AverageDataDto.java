package com.gabchak.weather.dto;

import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class AverageDataDto {
  private Integer id;
  private Double temperature;
  private Double pressure;
  private Double humidity;
  private Double airQuality;
  private Double windSpeed;
  private LocalDate date;
}
