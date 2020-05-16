package com.gabchak.example.rest;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.gabchak.example.config.WebMvcConfiguration;
import com.gabchak.example.models.User;
import com.gabchak.example.security.config.CorsProperties;
import com.gabchak.example.security.jwt.JwtTokenProvider;
import com.gabchak.example.services.UserService;
import java.time.LocalDate;
import java.util.Optional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserController.class)
@WithMockUser(authorities = "ROLE_FREE_USER", username = "test@gmail.com")
class UserControllerTest {
  public static final String SUBSCRIBE =
      WebMvcConfiguration.API_PREFIX + UserController.CRUD_PATH + UserController.SUBSCRIBE;

  @MockBean
  private UserService userService;
  @MockBean
  private JwtTokenProvider jwtTokenProvider;
  @MockBean
  private CorsProperties corsProperties;
  @MockBean
  private AuthenticationManager authenticationManager;
  @Autowired
  private UserController userController;
  @Autowired
  private MockMvc mockMvc;

  @SneakyThrows
  @Test
  void subscribe() {
    LocalDate subscriptionEndDate = LocalDate.now().plusMonths(1);
    User user = new User();
    user.setSubscription(subscriptionEndDate);
    when(userService.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));

    mockMvc
        .perform(get(SUBSCRIBE))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.date", is(subscriptionEndDate.toString())));
  }

  @SneakyThrows
  @Test
  void findSubscription() {
    LocalDate subscriptionEndDate = LocalDate.now().plusMonths(1);
    when(userService.subscribe(ArgumentMatchers.anyString())).thenReturn(subscriptionEndDate);
    mockMvc
        .perform(put(SUBSCRIBE))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.date", is(subscriptionEndDate.toString())));
  }
}