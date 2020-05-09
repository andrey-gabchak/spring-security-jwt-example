package com.gabchak.weather.services.security.jwt;

import com.gabchak.weather.models.Role;
import com.gabchak.weather.models.User;
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
        mapToGrantedAuthorities(user.getRoles())
    );
  }

  private static List<GrantedAuthority> mapToGrantedAuthorities(Collection<Role> userRoles) {
    return userRoles.stream()
        .map(role ->
            new SimpleGrantedAuthority(
                GRANTED_AUTHORITY_PREFIX + role.getName())
        ).collect(Collectors.toList());
  }
}
