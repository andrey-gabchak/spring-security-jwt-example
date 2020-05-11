package com.gabchak.example.dto.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@RequiredArgsConstructor
public class AuthResponse {

  private final String token;
  private final String email;
  private final Collection<String> roles;
}
