package com.gabchak.example.services;

import static com.gabchak.example.constants.TestStaticModels.USER;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;


import com.gabchak.example.dto.enums.Roles;
import com.gabchak.example.dto.jwt.RegisterRequest;
import com.gabchak.example.models.Role;
import com.gabchak.example.models.User;
import com.gabchak.example.repositories.UserRepository;
import com.gabchak.example.security.jwt.JwtUser;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

  public static final String[] GIVEN_FIELDS =
      {"id", "firstName", "lastName", "email", "password"};
  @Mock
  private UserRepository userRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  @InjectMocks
  private UserService userService;
  private User user;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setFirstName(USER.getFirstName());
    user.setLastName(USER.getLastName());
    user.setId(USER.getId());
    user.setEmail(USER.getEmail());
    user.setPassword(USER.getPassword());
    List<Role> roles = Collections.singletonList(Role.fromEnum(Roles.FREE_USER));
    user.setRoles(roles);
  }

  @Test
  void save() {
    when(userRepository.save(user)).thenReturn(user);
    User actual = userService.save(user);
    comparingUsersWithoutRoles(actual, USER);
    String actualRoleName = actual.getRoles()
        .stream().findFirst().map(Role::getName).orElse("");
    Assertions.assertThat(actualRoleName).isEqualTo(Roles.FREE_USER.name());
  }

  @Test
  void findByEmail_found() {
    String email = "email";
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
    Optional<User> optionalUser = userService.findByEmail(email);
    if (optionalUser.isPresent()) {
      comparingUsersWithoutRoles(optionalUser.get(), user);
    } else {
      Assertions.fail("user not found");
    }
  }
  @Test
  void findByEmail_notFound() {
    String email = "email";
    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
    Optional<User> optionalUser = userService.findByEmail(email);
    Assertions.assertThatThrownBy(() ->
        optionalUser.orElseThrow(Assertions.fail("user not found")))
        .isInstanceOf(AssertionError.class);
  }

  @Test
  void register() {
    User testUser = getNewTestUser();
    List<Role> roles = Collections.singletonList(Role.fromEnum(Roles.FREE_USER));
    testUser.setRoles(roles);
    RegisterRequest request = getRegisterRequest();
    when(passwordEncoder.encode(any())).thenReturn("password");
    when(userRepository.save(any())).thenReturn(testUser);
    JwtUser actual = userService.register(request);
    Assertions.assertThat(actual)
        .isEqualToComparingOnlyGivenFields(user,
            "id", "firstName", "lastName", "password");
    Assertions.assertThat(actual.getUsername()).isEqualTo(user.getEmail());
    String actualRoleName = actual.getAuthorities()
        .stream().findFirst().map(GrantedAuthority::getAuthority).orElse("");
    Assertions.assertThat(actualRoleName).isEqualTo("ROLE_" + Roles.FREE_USER.name());
  }

  private User getNewTestUser() {
    User testUser = new User();
    testUser.setFirstName(user.getFirstName());
    testUser.setLastName(user.getLastName());
    testUser.setId(user.getId());
    testUser.setEmail(user.getEmail());
    testUser.setPassword(user.getPassword());
    return testUser;
  }

  private RegisterRequest getRegisterRequest() {
    RegisterRequest request = new RegisterRequest();
    request.setEmail(user.getEmail());
    request.setPassword(user.getPassword());
    request.setFirstName(user.getFirstName());
    request.setLastName(user.getLastName());
    return request;
  }

  @Test
  void subscribe() {
    String email = "admin@gmail.com";
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);
    LocalDate actual = userService.subscribe(email);
    Assertions.assertThat(actual).isEqualTo(LocalDate.now().plusMonths(1));
  }

  @Test
  void subscribe_notFound() {
    String email = "admin@gmail.com";
    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
    Assertions.assertThatThrownBy(() ->
        userService.subscribe(email))
        .isInstanceOf(UsernameNotFoundException.class);
  }

  private void comparingUsersWithoutRoles(User actual, User expected) {
    Assertions.assertThat(actual)
        .isEqualToComparingOnlyGivenFields(expected, GIVEN_FIELDS);
  }
}