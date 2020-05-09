package com.gabchak.example.constants;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DataJson {
  private Integer id;
  private Double temperature;
  private Double pressure;
  private Double humidity;
  private Double airQuality;
  private Double windSpeed;
  private String date;
  private boolean isIdExcluded;

  public String jsonDouble() {
    StringBuilder json = new StringBuilder();
    json.append("{");
    if (!isIdExcluded) {
      json.append("\"id\": ").append(id).append(", ");
    }
    return json.append("\"airQuality\": ")
        .append(airQuality).append(", ")
        .append("\"date\": \"").append(date).append("\", ")
        .append("\"humidity\": ").append(humidity).append(", ")
        .append("\"pressure\": ").append(pressure).append(", ")
        .append("\"temperature\": ").append(temperature).append(", ")
        .append("\"windSpeed\": ").append(windSpeed).append("}")
        .toString();
  }

  public String jsonInt() {
    StringBuilder json = new StringBuilder();
    json.append("{");
    if (!isIdExcluded) {
      json.append("\"id\": ").append(id).append(", ");
    }
    return json.append("\"airQuality\": ")
        .append(airQuality.intValue()).append(", ")
        .append("\"date\": \"").append(date).append("\", ")
        .append("\"humidity\": ").append(humidity.intValue()).append(", ")
        .append("\"pressure\": ").append(pressure.intValue()).append(", ")
        .append("\"temperature\": ").append(temperature.intValue()).append(", ")
        .append("\"windSpeed\": ").append(windSpeed.intValue()).append("}")
        .toString();
  }

  public DataJson excludeId() {
    this.isIdExcluded = true;
    return this;
  }

  public DataJson setId(Integer id) {
    this.id = id;
    return this;
  }

  public DataJson setTemperature(Double temperature) {
    this.temperature = temperature;
    return this;
  }

  public DataJson setPressure(Double pressure) {
    this.pressure = pressure;
    return this;
  }

  public DataJson setHumidity(Double humidity) {
    this.humidity = humidity;
    return this;
  }

  public DataJson setAirQuality(Double airQuality) {
    this.airQuality = airQuality;
    return this;
  }

  public DataJson setWindSpeed(Double windSpeed) {
    this.windSpeed = windSpeed;
    return this;
  }

  public DataJson setDate(String date) {
    this.date = date;
    return this;
  }
}
