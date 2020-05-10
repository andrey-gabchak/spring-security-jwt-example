package com.gabchak.example.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
  }

  @JsonIgnore
  public Integer getId() {
    return id;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  public String getFirstname() {
    return firstName;
  }

  public String getLastname() {
    return lastName;
  }

  @JsonIgnore
  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
