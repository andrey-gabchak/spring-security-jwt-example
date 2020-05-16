package com.gabchak.example.rest;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.gabchak.example.config.WebMvcConfiguration;
import com.gabchak.example.dto.jwt.AuthResponse;
import com.gabchak.example.dto.jwt.JwtUser;
import com.gabchak.example.security.config.CorsProperties;
import com.gabchak.example.security.jwt.JwtTokenProvider;
import com.gabchak.example.services.UserService;
import java.util.Collections;
import java.util.List;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser
@WebMvcTest(controllers = AuthenticationController.class)
class AuthenticationControllerTest {

  public static final String LOGIN =
      WebMvcConfiguration.API_PREFIX + AuthenticationController.LOGIN;
  public static final String REGISTER =
      WebMvcConfiguration.API_PREFIX + AuthenticationController.REGISTER;

  @MockBean
  private CorsProperties corsProperties;
  @MockBean
  private AuthenticationManager authenticationManager;
  @MockBean
  private JwtTokenProvider jwtTokenProvider;
  @MockBean
  private UserDetailsService userDetailsService;
  @MockBean
  private UserService userService;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private AuthenticationController controller;

  @SneakyThrows
  @Test
  void login_success() {
    String email = "test@gmail.com";
    JSONObject json = new JSONObject();
    json.put("email", email);
    json.put("password", "pass");
    JwtUser jwtUser = new JwtUser();
    when(userDetailsService.loadUserByUsername(email)).thenReturn(jwtUser);

    String token = "token value";
    String role = "ROLE_FREE_USER";
    List<String> authorities = Collections.singletonList(role);
    AuthResponse authResponse = new AuthResponse(token, email, authorities);
    when(jwtTokenProvider.buildAuthResponse(jwtUser)).thenReturn(authResponse);

    mockMvc.perform(
        post(LOGIN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString())
    )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.token", is(token)))
        .andExpect(jsonPath("$.email", is(email)))
        .andExpect(jsonPath("$.roles.[0]", is(role)));
  }

  @SneakyThrows
  @Test
  void login_notFound() {
    JSONObject json = new JSONObject();
    json.put("email", "non@exist.com");
    json.put("password", "nonexist");

    doThrow(UsernameNotFoundException.class)
        .when(userDetailsService).loadUserByUsername(Mockito.any());

    mockMvc.perform(
        post(LOGIN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.httpStatus", is("BAD_REQUEST")));
  }

  @SneakyThrows
  @Test
  void login_failEmailValidation_missingDomainName() {
    JSONObject json = new JSONObject();
    json.put("email", "non@.com");
    json.put("password", "nonexist");

    doThrow(UsernameNotFoundException.class)
        .when(userDetailsService).loadUserByUsername(Mockito.any());

    mockMvc.perform(
        post(LOGIN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
        .andExpect(status().is4xxClientError());
  }

  @SneakyThrows
  @Test
  void login_failEmailValidation_missingDomainZone() {
    JSONObject json = new JSONObject();
    json.put("email", "non@test.");
    json.put("password", "nonexist");

    doThrow(UsernameNotFoundException.class)
        .when(userDetailsService).loadUserByUsername(Mockito.any());

    mockMvc.perform(
        post(LOGIN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
        .andExpect(status().is4xxClientError());
  }

  @SneakyThrows
  @Test
  void login_failEmailValidation_missingDomain() {
    JSONObject json = new JSONObject();
    json.put("email", "non@");
    json.put("password", "nonexist");

    doThrow(UsernameNotFoundException.class)
        .when(userDetailsService).loadUserByUsername(Mockito.any());

    mockMvc.perform(
        post(LOGIN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
        .andExpect(status().is4xxClientError());
  }

  @SneakyThrows
  @Test
  void login_failEmailValidation_missingMailBoxName() {
    JSONObject json = new JSONObject();
    json.put("email", "@gmail.com");
    json.put("password", "nonexist");

    doThrow(UsernameNotFoundException.class)
        .when(userDetailsService).loadUserByUsername(Mockito.any());

    mockMvc.perform(
        post(LOGIN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
        .andExpect(status().is4xxClientError());
  }

  @SneakyThrows
  @Test
  void login_failEmailValidation_missingPassword() {
    JSONObject json = new JSONObject();
    json.put("email", "test@gmail.com");
    json.put("password", "");

    doThrow(UsernameNotFoundException.class)
        .when(userDetailsService).loadUserByUsername(Mockito.any());

    mockMvc.perform(
        post(LOGIN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void register() {
  }
}