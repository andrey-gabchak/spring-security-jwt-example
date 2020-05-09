package com.gabchak.weather.dto.jwt;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest extends LoginRequest {
  @NotEmpty
  private String firstName;
  @NotEmpty
  private String lastName;
}
