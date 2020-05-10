package com.gabchak.example.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class JwtUser implements UserDetails {

  private final Integer id;
  private final String username;
  private final String firstName;
  private final String lastName;
  private final String password;
  /**
   * Field is not used in {@link com.gabchak.example.models.User},
   * so a user is always enabled.
   */
  private final boolean enabled;
  @JsonIgnore
  private final boolean accountNonExpired;
  @JsonIgnore
  private final boolean accountNonLocked;
  @JsonIgnore
  private final boolean credentialsNonExpired;

  private final Collection<? extends GrantedAuthority> authorities;

  /**
   * Constructor of the class.
   *
   * @param id          user integer id
   * @param username    email of user
   * @param firstName   name of user
   * @param lastName    surname of user
   * @param password    password of user
   * @param authorities roles of user
   *
   * Fields 'enabled', 'accountNonExpired'
   * 'accountNonLocked', 'credentialsNonExpired'
   * are not used in {@link com.gabchak.example.models.User},
   * so always true.
   */
  public JwtUser(
      Integer id,
      String username,
      String firstName,
      String lastName,
      String password,
      Collection<? extends GrantedAuthority> authorities
  ) {
    this.id = id;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.password = password;
    this.authorities = authorities;
    this.enabled = true;
    this.accountNonExpired = true;
    this.accountNonLocked = true;
    this.credentialsNonExpired = true;
  }
}
