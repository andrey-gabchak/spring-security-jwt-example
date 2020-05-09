package com.gabchak.weather.dto;

import com.gabchak.weather.dto.validation.OnCreate;
import com.gabchak.weather.dto.validation.OnUpdate;
import java.time.LocalDate;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class WeatherDataDto {
  @Null(groups = {OnCreate.class})
  @NotNull(groups = {OnUpdate.class})
  private Integer id;
  @Min(value = -100, groups = {OnCreate.class, OnUpdate.class})
  @Max(value = 100, groups = {OnCreate.class, OnUpdate.class})
  private Integer temperature;
  @Min(value = 300, groups = {OnCreate.class, OnUpdate.class})
  private Integer pressure;
  @Positive(groups = {OnCreate.class, OnUpdate.class})
  private Integer humidity;
  @Positive(groups = {OnCreate.class, OnUpdate.class})
  private Integer airQuality;
  @Positive(groups = {OnCreate.class, OnUpdate.class})
  private Integer windSpeed;
  @NotNull(groups = {OnCreate.class, OnUpdate.class})
  private LocalDate date;
}
