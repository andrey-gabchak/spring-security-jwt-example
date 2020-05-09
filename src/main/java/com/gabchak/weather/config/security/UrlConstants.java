package com.gabchak.weather.config.security;

import static com.gabchak.weather.rest.AuthenticationController.LOGIN;
import static com.gabchak.weather.rest.AuthenticationController.REGISTER;


import com.gabchak.weather.config.WebMvcConfiguration;
import com.gabchak.weather.rest.UserController;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UrlConstants {

  public static String[] PUBLIC_ACCESS = {
      WebMvcConfiguration.API_PREFIX + LOGIN,
      WebMvcConfiguration.API_PREFIX + REGISTER,
  };

  public static String[] FREE_USER_ACCESS = {
      WebMvcConfiguration.API_PREFIX + UserController.CRUD_PATH + UserController.SUBSCRIBE
  };
  public static String[] PAID_USER_ACCESS = {
  };

  public static String[] ADMIN_ACCESS = {
      "/swagger-ui.html",
      WebMvcConfiguration.API_PREFIX + UserController.CRUD_PATH + UserController.SUBSCRIBE
  };
}
