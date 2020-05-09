package com.gabchak.weather.services.security.jwt;

import static org.junit.jupiter.api.Assertions.*;


import com.gabchak.weather.constants.TestStaticModels;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JwtUserTest {


  @Test
  void getId() {
    Assertions.assertEquals(1, TestStaticModels.JWT_USER.getId());
  }

  @Test
  void getUsername() {
    Assertions.assertEquals("admin@gmail.com", TestStaticModels.JWT_USER.getUsername());
  }

  @Test
  void isAccountNonExpired() {
    assertTrue(TestStaticModels.JWT_USER.isAccountNonExpired());
  }

  @Test
  void isAccountNonLocked() {
    assertTrue(TestStaticModels.JWT_USER.isAccountNonLocked());
  }

  @Test
  void isCredentialsNonExpired() {
    assertTrue(TestStaticModels.JWT_USER.isCredentialsNonExpired());
  }

  @Test
  void getFirstname() {
    Assertions.assertEquals("firstName", TestStaticModels.JWT_USER.getFirstname());

  }

  @Test
  void getLastname() {
    Assertions.assertEquals("lastName", TestStaticModels.JWT_USER.getLastname());
  }

  @Test
  void getPassword() {
    Assertions.assertEquals("password", TestStaticModels.JWT_USER.getPassword());
  }

  @Test
  void getAuthorities() {
    Assertions.assertEquals(TestStaticModels.AUTHORITIES, TestStaticModels.JWT_USER.getAuthorities());
  }

  @Test
  void isEnabled() {
    assertTrue(TestStaticModels.JWT_USER.isEnabled());
  }
}