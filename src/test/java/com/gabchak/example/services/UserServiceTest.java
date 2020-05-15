package com.gabchak.example.services;

import static com.gabchak.example.constants.TestStaticModels.JWT_FREE_USER;
import static com.gabchak.example.constants.TestStaticModels.USER;
import static org.mockito.Mockito.when;


import com.gabchak.example.dto.enums.Roles;
import com.gabchak.example.dto.jwt.JwtUser;
import com.gabchak.example.dto.jwt.RegisterRequest;
import com.gabchak.example.dto.mapper.RegisterRequestUserMapper;
import com.gabchak.example.dto.mapper.UserJwtUserMapper;
import com.gabchak.example.models.Role;
import com.gabchak.example.models.User;
import com.gabchak.example.repositories.UserRepository;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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
  @Mock
  private RegisterRequestUserMapper registerRequestUserMapper;
  @Mock
  private UserJwtUserMapper userJwtUserMapper;
  @InjectMocks
  private UserService userService;
  private User freeUser;

  @BeforeEach
  void setUp() {
    freeUser = new User();
    freeUser.setFirstName(USER.getFirstName());
    freeUser.setLastName(USER.getLastName());
    freeUser.setId(USER.getId());
    freeUser.setEmail(USER.getEmail());
    freeUser.setPassword(USER.getPassword());
    List<Role> roles = Collections.singletonList(Role.fromEnum(Roles.FREE_USER));
    freeUser.setRoles(roles);
  }

  @Test
  void save() {
    when(userRepository.save(freeUser)).thenReturn(freeUser);
    User actual = userService.save(freeUser);
    comparingUsersWithoutRoles(actual, USER);
    String actualRoleName = actual.getRoles()
        .stream().findFirst().map(Role::getName).orElse("");
    Assertions.assertThat(actualRoleName).isEqualTo(Roles.FREE_USER.name());
  }

  @Test
  void findByEmail_found() {
    String email = "email";
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(freeUser));
    Optional<User> optionalUser = userService.findByEmail(email);
    if (optionalUser.isPresent()) {
      comparingUsersWithoutRoles(optionalUser.get(), freeUser);
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
    RegisterRequest request = getRegisterRequest();
    when(registerRequestUserMapper.map(request, User.class)).thenReturn(freeUser);
    when(passwordEncoder.encode(ArgumentMatchers.any())).thenReturn("password");
    when(userRepository.save(ArgumentMatchers.any())).thenReturn(freeUser);
    when(userJwtUserMapper.map(freeUser, JwtUser.class)).thenReturn(JWT_FREE_USER);
    JwtUser actual = userService.register(request);
    Assertions.assertThat(actual)
        .isEqualToComparingOnlyGivenFields(JWT_FREE_USER,
            "id", "firstName", "lastName", "password");
    Assertions.assertThat(actual.getUsername()).isEqualTo(freeUser.getEmail());
    String actualRoleName = actual.getAuthorities()
        .stream().findFirst().map(GrantedAuthority::getAuthority).orElse("");
    Assertions.assertThat(actualRoleName).isEqualTo("ROLE_" + Roles.FREE_USER.name());
  }

  private RegisterRequest getRegisterRequest() {
    RegisterRequest request = new RegisterRequest();
    request.setEmail(freeUser.getEmail());
    request.setPassword(freeUser.getPassword());
    request.setFirstName(freeUser.getFirstName());
    request.setLastName(freeUser.getLastName());
    return request;
  }

  @Test
  void subscribe() {
    String email = "admin@gmail.com";
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(freeUser));
    when(userRepository.save(freeUser)).thenReturn(freeUser);
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