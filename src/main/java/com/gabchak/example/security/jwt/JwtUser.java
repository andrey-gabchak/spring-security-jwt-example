package com.gabchak.example.security.jwt;

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
  private boolean enabled;
  @JsonIgnore
  private boolean accountNonExpired;
  @JsonIgnore
  private boolean accountNonLocked;
  @JsonIgnore
  private boolean credentialsNonExpired;
  private Collection<? extends GrantedAuthority> authorities;
}
