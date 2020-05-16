package com.gabchak.example.constants;

import com.gabchak.example.dto.enums.Roles;
import com.gabchak.example.dto.jwt.JwtUser;
import com.gabchak.example.models.Role;
import com.gabchak.example.models.User;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestStaticModels {

  public static final List<Role> ROLES = List.of(
      Role.fromEnum(Roles.ADMIN),
      Role.fromEnum(Roles.PAID_USER),
      Role.fromEnum(Roles.FREE_USER));
  public static final List<SimpleGrantedAuthority> AUTHORITIES =
      ROLES.stream()
      .map(role ->
          new SimpleGrantedAuthority("ROLE_" + role.getName()))
      .collect(Collectors.toList());
  public static final JwtUser JWT_ADMIN = new JwtUser();
  public static final JwtUser JWT_FREE_USER = new JwtUser();

  public static final User USER;

  static {
    USER = new User();
    USER.setEmail("admin@gmail.com");
    USER.setPassword("password");
    USER.setFirstName("firstName");
    USER.setLastName("lastName");
    USER.setId(1);
    USER.setRoles(ROLES);

    JWT_ADMIN.setId(1);
    JWT_ADMIN.setUsername("admin@gmail.com");
    JWT_ADMIN.setFirstName("firstName");
    JWT_ADMIN.setLastName("lastName");
    JWT_ADMIN.setPassword("password");
    JWT_ADMIN.setAuthorities(AUTHORITIES);

    JWT_FREE_USER.setId(1);
    JWT_FREE_USER.setUsername("admin@gmail.com");
    JWT_FREE_USER.setFirstName("firstName");
    JWT_FREE_USER.setLastName("lastName");
    JWT_FREE_USER.setPassword("password");
    JWT_FREE_USER.setAuthorities(
        Collections.singletonList(
            new SimpleGrantedAuthority(
                "ROLE_".concat(Roles.FREE_USER.name()
                )
            )
        ));
  }
}
