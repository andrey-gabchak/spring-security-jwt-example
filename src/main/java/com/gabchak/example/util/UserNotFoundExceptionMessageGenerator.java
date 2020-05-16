package com.gabchak.example.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserNotFoundExceptionMessageGenerator {

  public static String generateMessage(String email) {
    return "User with email '" + email + "' not found";
  }
}
