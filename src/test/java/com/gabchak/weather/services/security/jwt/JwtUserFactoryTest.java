package com.gabchak.weather.services.security.jwt;

import static com.gabchak.weather.constants.TestStaticModels.AUTHORITIES;
import static com.gabchak.weather.constants.TestStaticModels.JWT_USER;
import static com.gabchak.weather.constants.TestStaticModels.USER;


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