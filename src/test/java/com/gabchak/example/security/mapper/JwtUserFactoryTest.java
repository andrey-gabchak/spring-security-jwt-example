package com.gabchak.example.security.mapper;

import static com.gabchak.example.constants.TestStaticModels.AUTHORITIES;
import static com.gabchak.example.constants.TestStaticModels.JWT_USER;
import static com.gabchak.example.constants.TestStaticModels.USER;


import com.gabchak.example.security.jwt.JwtUser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class JwtUserFactoryTest {

  @Test
  void create() {
    JwtUser actual = JwtUserFactory.create(USER);
    Assertions.assertThat(actual).isEqualToComparingOnlyGivenFields(JWT_USER,
        "id", "username", "firstName", "lastName", "password", "enabled");
    Assertions.assertThat(actual.getAuthorities()).isEqualTo(AUTHORITIES);
  }
}