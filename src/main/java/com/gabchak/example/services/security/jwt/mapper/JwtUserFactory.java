package com.gabchak.example.services.security.jwt.mapper;

import com.gabchak.example.models.Role;
import com.gabchak.example.models.User;
import com.gabchak.example.services.security.jwt.JwtUser;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUserFactory {

  public static final String GRANTED_AUTHORITY_PREFIX = "ROLE_";

  /**
   * The method builds a new {@link JwtUser}
   * from {@link User}.
   *
   * @param user user model
   * @return a new security user
   */
  public static JwtUser create(User user) {
    return new JwtUser(
        user.getId(),
        user.getEmail(),
        user.getFirstName(),
        user.getLastName(),
        user.getPassword(),
        mapRolesToAuthorities(user.getRoles())
    );
  }

  private static List<GrantedAuthority> mapRolesToAuthorities(Collection<Role> userRoles) {
    return userRoles.stream()
        .map(role ->
            new SimpleGrantedAuthority(
                GRANTED_AUTHORITY_PREFIX + role.getName())
        ).collect(Collectors.toList());
  }
}
