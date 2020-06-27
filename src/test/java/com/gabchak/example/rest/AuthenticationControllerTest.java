package com.gabchak.example.rest;

import com.gabchak.example.DtoTestFactory;
import com.gabchak.example.EntityTestFactory;
import com.gabchak.example.JsonTestFactory;
import com.gabchak.example.dto.jwt.AuthResponse;
import com.gabchak.example.dto.jwt.JwtUser;
import com.gabchak.example.dto.jwt.LoginRequest;
import com.gabchak.example.dto.jwt.RegisterRequest;
import com.gabchak.example.models.User;
import com.gabchak.example.security.jwt.JwtTokenProvider;
import com.gabchak.example.services.UserService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

  @Mock
  private AuthenticationManager authenticationManager;
  @Mock
  private JwtTokenProvider jwtTokenProvider;
  @Mock
  private UserDetailsService userDetailsService;
  @Mock
  private UserService userService;
  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders
        .standaloneSetup(new AuthenticationController(
            authenticationManager,
            jwtTokenProvider,
            userDetailsService,
            userService
        ))
        .build();
  }

  @SneakyThrows
  @Test
  void login_success() {
    LoginRequest loginRequest = DtoTestFactory.buildLoginRequest();
    User user = EntityTestFactory.buildUser();
    JwtUser jwtUser = DtoTestFactory.buildJwtUser(user);
    String token = RandomStringUtils.randomAlphabetic(50);
    String role = "ROLE_FREE_USER";
    AuthResponse authResponse = new AuthResponse(
        token,
        loginRequest.getEmail(),
        Collections.singletonList(role)
    );

    Mockito
        .when(userDetailsService.loadUserByUsername(loginRequest.getEmail()))
        .thenReturn(jwtUser);
    Mockito
        .when(jwtTokenProvider.buildAuthResponse(jwtUser))
        .thenReturn(authResponse);

    mockMvc.perform(
        post(AuthenticationController.LOGIN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                JsonTestFactory.OBJECT_WRITER.writeValueAsString(loginRequest)
            )
    )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().string(
            JsonTestFactory.OBJECT_WRITER.writeValueAsString(authResponse)
        ));
  }

  @SneakyThrows
  @Test
  void login_validationFail_missingEmailDomainName() {
    JSONObject json = new JSONObject();
    json.put("email", "non@.com");
    json.put("password", "nonexist");

    mockMvc.perform(
        post(AuthenticationController.LOGIN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString())
    )
        .andExpect(status().is4xxClientError());
  }

  @SneakyThrows
  @Test
  void login_validationFail_missingEmailDomainZone() {
    JSONObject json = new JSONObject();
    json.put("email", "non@test.");
    json.put("password", "nonexist");

    mockMvc.perform(
        post(AuthenticationController.LOGIN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
        .andExpect(status().is4xxClientError());
  }

  @SneakyThrows
  @Test
  void login_validationFail_missingEmailDomain() {
    JSONObject json = new JSONObject();
    json.put("email", "non@");
    json.put("password", "nonexist");

    mockMvc.perform(
        post(AuthenticationController.LOGIN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
        .andExpect(status().is4xxClientError());
  }

  @SneakyThrows
  @Test
  void login_validationFail_missingMailBoxName() {
    JSONObject json = new JSONObject();
    json.put("email", "@gmail.com");
    json.put("password", "nonexist");

    mockMvc.perform(
        post(AuthenticationController.LOGIN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
        .andExpect(status().is4xxClientError());
  }

  @SneakyThrows
  @Test
  void login_validationFail_emptyPassword() {
    JSONObject json = new JSONObject();
    json.put("email", "test@gmail.com");
    json.put("password", "");

    mockMvc.perform(
        post(AuthenticationController.LOGIN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
        .andExpect(status().is4xxClientError());
  }

  @SneakyThrows
  @Test
  void register_success() {
    String token = RandomStringUtils.randomAlphabetic(50);
    String role = "ROLE_FREE_USER";
    RegisterRequest registerRequest = DtoTestFactory.buildRegisterRequest();
    User user = EntityTestFactory.buildUser(registerRequest);
    JwtUser jwtUser = DtoTestFactory.buildJwtUser(user);

    AuthResponse authResponse = new AuthResponse(
        token,
        jwtUser.getUsername(),
        Collections.singletonList(role));

    Mockito
        .when(userService.register(Mockito.any(RegisterRequest.class)))
        .thenReturn(jwtUser);
    Mockito
        .when(jwtTokenProvider.buildAuthResponse(jwtUser))
        .thenReturn(authResponse);

    String registerJson = JsonTestFactory.OBJECT_WRITER
        .writeValueAsString(registerRequest);

    mockMvc.perform(
        post(AuthenticationController.REGISTER)
            .contentType(MediaType.APPLICATION_JSON)
            .content(registerJson))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().string(
            JsonTestFactory.OBJECT_WRITER
                .writeValueAsString(authResponse)
        ));
  }

  @SneakyThrows
  @Test
  void register_validationFail_missingDomainName() {
    JSONObject json = new JSONObject();
    json.put("email", "non@.com");
    json.put("password", "nonexist");
    json.put("firstName", "firstName");
    json.put("lastName", "lastName");

    mockMvc.perform(
        post(AuthenticationController.LOGIN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
        .andExpect(status().is4xxClientError());
  }

  @SneakyThrows
  @Test
  void register_validationFail_missingEmailDomainZone() {
    JSONObject json = new JSONObject();
    json.put("email", "non@test.");
    json.put("password", "nonexist");
    json.put("firstName", "firstName");
    json.put("lastName", "lastName");

    mockMvc.perform(
        post(AuthenticationController.LOGIN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
        .andExpect(status().is4xxClientError());
  }

  @SneakyThrows
  @Test
  void register_validationFail_missingEmailDomain() {
    JSONObject json = new JSONObject();
    json.put("email", "non@");
    json.put("password", "nonexist");
    json.put("firstName", "firstName");
    json.put("lastName", "lastName");

    mockMvc.perform(
        post(AuthenticationController.LOGIN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
        .andExpect(status().is4xxClientError());
  }

  @SneakyThrows
  @Test
  void register_validationFail_missingMailBoxName() {
    JSONObject json = new JSONObject();
    json.put("email", "@gmail.com");
    json.put("password", "nonexist");
    json.put("firstName", "firstName");
    json.put("lastName", "lastName");

    mockMvc.perform(
        post(AuthenticationController.LOGIN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
        .andExpect(status().is4xxClientError());
  }

  @SneakyThrows
  @Test
  void register_validationFail_emptyPassword() {
    JSONObject json = new JSONObject();
    json.put("email", "test@gmail.com");
    json.put("password", "");
    json.put("firstName", "firstName");
    json.put("lastName", "lastName");

    mockMvc.perform(
        post(AuthenticationController.REGISTER)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
        .andExpect(status().is4xxClientError());
  }

  @SneakyThrows
  @Test
  void register_validationFail_emptyFirstName() {
    JSONObject json = new JSONObject();
    json.put("email", "test@gmail.com");
    json.put("password", "pass");
    json.put("firstName", "");
    json.put("lastName", "lastName");

    mockMvc.perform(
        post(AuthenticationController.REGISTER)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
        .andExpect(status().is4xxClientError());
  }

  @SneakyThrows
  @Test
  void register_validationFail_emptyLastName() {
    JSONObject json = new JSONObject();
    json.put("email", "test@gmail.com");
    json.put("password", "pass");
    json.put("firstName", "firstName");
    json.put("lastName", "");

    mockMvc.perform(
        post(AuthenticationController.REGISTER)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json.toString()))
        .andExpect(status().is4xxClientError());
  }
}