package com.gabchak.example.dto.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
public class JwtUser implements UserDetails {
  private Integer id;
  private String username;
  private String firstName;
  private String lastName;
  private String password;
  private Collection<? extends GrantedAuthority> authorities;
  /**
   * Fields below are not used in {@link com.gabchak.example.models.User},
   * so, always true.
   */
  private boolean enabled = Boolean.TRUE;
  @JsonIgnore
  private boolean accountNonExpired = Boolean.TRUE;
  @JsonIgnore
  private boolean accountNonLocked = Boolean.TRUE;
  @JsonIgnore
  private boolean credentialsNonExpired = Boolean.TRUE;
}
