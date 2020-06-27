package com.gabchak.example.services;

import com.gabchak.example.DtoTestFactory;
import com.gabchak.example.dto.enums.Roles;
import com.gabchak.example.dto.jwt.JwtUser;
import com.gabchak.example.dto.jwt.RegisterRequest;
import com.gabchak.example.dto.mapper.RegisterRequestUserMapper;
import com.gabchak.example.dto.mapper.UserJwtUserMapper;
import com.gabchak.example.models.Role;
import com.gabchak.example.models.User;
import com.gabchak.example.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.gabchak.example.constants.TestStaticModels.JWT_FREE_USER;
import static com.gabchak.example.constants.TestStaticModels.USER;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  private static final String TEST_EMAIL = "admin@gmail.com";
  public static final String[] COMPARING_FIELDS =
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
    Mockito.
        when(userRepository.save(freeUser))
        .thenReturn(freeUser);
    User actual = userService.save(freeUser);

    String actualRoleName = actual.getRoles()
        .stream().findFirst().map(Role::getName).orElse("");

    Assertions
        .assertThat(actual)
        .isEqualToComparingOnlyGivenFields(USER, COMPARING_FIELDS);
    Assertions
        .assertThat(actualRoleName)
        .isEqualTo(Roles.FREE_USER.name());
  }

  @Test
  void findByEmail_found() {
    Mockito
        .when(userRepository.findByEmail(TEST_EMAIL))
        .thenReturn(Optional.of(freeUser));

    Optional<User> optionalUser = userService.findByEmail(TEST_EMAIL);

    optionalUser
        .ifPresentOrElse(user -> Assertions
                .assertThat(user)
                .isEqualToComparingOnlyGivenFields(freeUser, COMPARING_FIELDS),
            () -> Assertions.fail("user not found"));
  }

  @Test
  void findByEmail_notFound() {
    Mockito
        .when(userRepository.findByEmail(TEST_EMAIL))
        .thenReturn(Optional.empty());

    Optional<User> optionalUser = userService.findByEmail(TEST_EMAIL);

    Assertions
        .assertThatThrownBy(() ->
            optionalUser.orElseThrow(Assertions.fail("user not found")))
        .isInstanceOf(AssertionError.class);
  }

  @Test
  void register() {
    RegisterRequest request = DtoTestFactory.getRegisterRequest(freeUser);
    Mockito
        .when(registerRequestUserMapper.map(request, User.class))
        .thenReturn(freeUser);
    Mockito
        .when(passwordEncoder.encode(ArgumentMatchers.any()))
        .thenReturn("password");
    Mockito
        .when(userRepository.save(ArgumentMatchers.any()))
        .thenReturn(freeUser);
    Mockito
        .when(userJwtUserMapper.map(freeUser, JwtUser.class))
        .thenReturn(JWT_FREE_USER);

    JwtUser actual = userService.register(request);

    String actualRoleName = actual.getAuthorities()
        .stream()
        .findFirst()
        .map(GrantedAuthority::getAuthority)
        .orElse("");
    Assertions.assertThat(actual)
        .isEqualToComparingOnlyGivenFields(JWT_FREE_USER,
            "id", "firstName", "lastName", "password");
    Assertions
        .assertThat(actual.getUsername())
        .isEqualTo(freeUser.getEmail());
    Assertions
        .assertThat(actualRoleName)
        .isEqualTo("ROLE_" + Roles.FREE_USER.name());
  }

  @Test
  void subscribe() {
    Mockito
        .when(userRepository.findByEmail(TEST_EMAIL))
        .thenReturn(Optional.of(freeUser));
    Mockito
        .when(userRepository.save(freeUser))
        .thenReturn(freeUser);

    LocalDate actual = userService.subscribe(TEST_EMAIL);

    Assertions
        .assertThat(actual)
        .isEqualTo(LocalDate.now().plusMonths(1));
  }

  @Test
  void subscribe_notFound() {
    String email = "admin@gmail.com";
    Mockito
        .when(userRepository.findByEmail(email))
        .thenReturn(Optional.empty());

    Assertions
        .assertThatThrownBy(() ->
            userService.subscribe(email))
        .isInstanceOf(UsernameNotFoundException.class);
  }
}