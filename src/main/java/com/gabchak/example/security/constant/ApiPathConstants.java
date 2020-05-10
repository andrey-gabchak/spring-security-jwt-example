package com.gabchak.example.security.constant;

import static com.gabchak.example.rest.AuthenticationController.LOGIN;
import static com.gabchak.example.rest.AuthenticationController.REGISTER;


import com.gabchak.example.config.WebMvcConfiguration;
import com.gabchak.example.rest.UserController;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiPathConstants {

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
      WebMvcConfiguration.API_PREFIX + UserController.CRUD_PATH + UserController.SUBSCRIBE
  };
}
