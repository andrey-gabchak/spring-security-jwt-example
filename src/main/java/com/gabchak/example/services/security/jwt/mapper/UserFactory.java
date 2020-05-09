package com.gabchak.example.services.security.jwt.mapper;

import com.gabchak.example.dto.enums.Roles;
import com.gabchak.example.dto.jwt.RegisterRequest;
import com.gabchak.example.models.Role;
import com.gabchak.example.models.User;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserFactory {

  public static User createNewUser(RegisterRequest registerRequest) {
    User user = new User();
    user.setEmail(registerRequest.getEmail());
    user.setFirstName(registerRequest.getFirstName());
    user.setLastName(registerRequest.getLastName());
    user.setRoles(Collections.singleton(Role.fromEnum(Roles.FREE_USER)));
    return user;
  }
}
