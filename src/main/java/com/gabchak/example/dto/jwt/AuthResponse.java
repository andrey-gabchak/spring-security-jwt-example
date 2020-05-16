package com.gabchak.example.dto.jwt;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {

  private final String token;
  private final String email;
  private final Collection<String> roles;
}
