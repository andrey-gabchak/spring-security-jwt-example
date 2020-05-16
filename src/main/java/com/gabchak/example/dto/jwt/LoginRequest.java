package com.gabchak.example.dto.jwt;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

  @Email
  @NotEmpty
  private String email;
  @NotEmpty
  private String password;
}
