package com.gabchak.example;

import com.gabchak.example.dto.jwt.JwtUser;
import com.gabchak.example.dto.jwt.LoginRequest;
import com.gabchak.example.dto.jwt.RegisterRequest;
import com.gabchak.example.models.Role;
import com.gabchak.example.models.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DtoTestFactory {

  public static LoginRequest buildLoginRequest() {
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail(randomEmail());
    loginRequest.setPassword(RandomStringUtils.randomAlphabetic(20));
    return loginRequest;
  }
  public static RegisterRequest buildRegisterRequest() {
    RegisterRequest request = new RegisterRequest();
    request.setEmail(randomEmail());
    request.setPassword(RandomStringUtils.randomAlphabetic(10));
    request.setFirstName(RandomStringUtils.randomAlphabetic(10));
    request.setLastName(RandomStringUtils.randomAlphabetic(10));
    return request;
  }

  public static JwtUser buildJwtUser(User user) {
    JwtUser jwtUser = new JwtUser();
    jwtUser.setId(user.getId());
    jwtUser.setUsername(user.getEmail());
    jwtUser.setFirstName(user.getFirstName());
    jwtUser.setLastName(user.getLastName());
    jwtUser.setPassword(user.getPassword());
    jwtUser.setAuthorities(mapRolesToAuthorities(user.getRoles()));
    return jwtUser;
  }

  public static List<SimpleGrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
    return roles
        .stream()
        .map(role ->
            new SimpleGrantedAuthority("ROLE_" + role.getName()))
        .collect(Collectors.toList());
  }

  public static String randomEmail() {
    return RandomStringUtils.randomAlphabetic(10)
        .concat("@")
        .concat(RandomStringUtils.randomAlphabetic(8))
        .concat(".")
        .concat(RandomStringUtils.randomAlphabetic(3));
  }
}
