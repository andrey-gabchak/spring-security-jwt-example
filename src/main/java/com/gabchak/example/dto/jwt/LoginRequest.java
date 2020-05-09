package com.gabchak.example.dto.jwt;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

  @NotEmpty
  private String email;
  @NotEmpty
  private String password;
}
