package com.gabchak.example.constants;

import com.gabchak.example.dto.enums.Roles;
import com.gabchak.example.models.Role;
import com.gabchak.example.models.User;
import com.gabchak.example.dto.jwt.JwtUser;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestStaticModels {

  public static final List<Role> ROLES = Arrays.asList(
      Role.fromEnum(Roles.ADMIN),
      Role.fromEnum(Roles.PAID_USER),
      Role.fromEnum(Roles.FREE_USER));
  public static final List<SimpleGrantedAuthority> AUTHORITIES =
      ROLES.stream()
      .map(role ->
          new SimpleGrantedAuthority("ROLE_" + role.getName()))
      .collect(Collectors.toList());
  public static final JwtUser JWT_USER = new JwtUser();

  public static final User USER;

  static {
    USER = new User();
    USER.setEmail("admin@gmail.com");
    USER.setPassword("password");
    USER.setFirstName("firstName");
    USER.setLastName("lastName");
    USER.setId(1);
    USER.setRoles(ROLES);

    JWT_USER.setId(1);
    JWT_USER.setUsername("admin@gmail.com");
    JWT_USER.setFirstName("firstName");
    JWT_USER.setLastName("lastName");
    JWT_USER.setPassword("password");
    JWT_USER.setAuthorities(AUTHORITIES);
  }
}
