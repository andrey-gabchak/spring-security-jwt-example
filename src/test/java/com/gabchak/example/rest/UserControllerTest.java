package com.gabchak.example.rest;

import com.gabchak.example.EntityTestFactory;
import com.gabchak.example.JsonTestFactory;
import com.gabchak.example.dto.SubscriptionDto;
import com.gabchak.example.models.User;
import com.gabchak.example.services.UserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
  public static final String SUBSCRIBE = UserController.CRUD_PATH
          + UserController.SUBSCRIBE;
  public static final String TEST_EMAIL = "test@gmail.com";
  @Mock
  private UserService userService;
  @Mock
  private Principal principal;
  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders
        .standaloneSetup(new UserController(userService))
        .build();
    when(principal.getName())
        .thenReturn(TEST_EMAIL);
  }

  @SneakyThrows
  @Test
  void subscribe() {
    LocalDate subscriptionEndDate = LocalDate.now().plusMonths(1);
    User user = EntityTestFactory.buildUser();
    user.setEmail(TEST_EMAIL);
    user.setSubscription(subscriptionEndDate);
    SubscriptionDto expected = new SubscriptionDto(user.getSubscription());
    when(userService.findByEmail(TEST_EMAIL))
        .thenReturn(Optional.of(user));

    mockMvc
        .perform(get(SUBSCRIBE).principal(principal))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().string(
            JsonTestFactory.OBJECT_WRITER.writeValueAsString(expected)));
  }

  @SneakyThrows
  @Test
  void findSubscription() {
    LocalDate subscriptionEndDate = LocalDate.now().plusMonths(1);
    SubscriptionDto expected = new SubscriptionDto(subscriptionEndDate);
    when(userService.subscribe(TEST_EMAIL))
        .thenReturn(subscriptionEndDate);
    mockMvc
        .perform(put(SUBSCRIBE).principal(principal))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().string(
            JsonTestFactory.OBJECT_WRITER.writeValueAsString(expected)));
  }
}