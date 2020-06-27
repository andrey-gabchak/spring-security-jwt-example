package com.gabchak.example;

import com.gabchak.example.dto.jwt.RegisterRequest;
import com.gabchak.example.models.User;

public class DtoTestFactory {
  public static RegisterRequest getRegisterRequest(User user) {
    RegisterRequest request = new RegisterRequest();
    request.setEmail(user.getEmail());
    request.setPassword(user.getPassword());
    request.setFirstName(user.getFirstName());
    request.setLastName(user.getLastName());
    return request;
  }
}
