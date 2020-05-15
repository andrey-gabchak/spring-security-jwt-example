package com.gabchak.example.dto.jwt;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class RegisterRequest extends LoginRequest {
  @NotEmpty
  private String firstName;
  @NotEmpty
  private String lastName;
}
