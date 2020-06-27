package com.gabchak.example;

import com.gabchak.example.dto.enums.Roles;
import com.gabchak.example.dto.jwt.RegisterRequest;
import com.gabchak.example.models.Role;
import com.gabchak.example.models.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.Collections;
import java.util.List;

public class EntityTestFactory {

  public static User buildUser(RegisterRequest registerRequest) {
    User user = new User();
    user.setEmail(registerRequest.getEmail());
    user.setPassword(registerRequest.getPassword());
    user.setFirstName(registerRequest.getFirstName());
    user.setLastName(registerRequest.getLastName());
    user.setId(RandomUtils.nextInt());
    user.setRoles(Collections.singletonList(Role.fromEnum(Roles.FREE_USER)));
    return user;
  }

  public static User buildUser() {
    User user = new User();
    user.setEmail(RandomStringUtils.randomAlphabetic(20));
    user.setPassword(RandomStringUtils.randomAlphabetic(10));
    user.setFirstName(RandomStringUtils.randomAlphabetic(10));
    user.setLastName(RandomStringUtils.randomAlphabetic(10));
    user.setId(RandomUtils.nextInt());
    user.setRoles(ALL_ROLES);
    return user;
  }

  public static final List<Role> ALL_ROLES = List.of(
      Role.fromEnum(Roles.ADMIN),
      Role.fromEnum(Roles.PAID_USER),
      Role.fromEnum(Roles.FREE_USER)
  );
}
