package com.gabchak.example.services;

import com.gabchak.example.DtoTestFactory;
import com.gabchak.example.EntityTestFactory;
import com.gabchak.example.dto.jwt.JwtUser;
import com.gabchak.example.dto.jwt.RegisterRequest;
import com.gabchak.example.dto.mapper.RegisterRequestUserMapper;
import com.gabchak.example.dto.mapper.UserJwtUserMapper;
import com.gabchak.example.models.User;
import com.gabchak.example.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

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

  @Test
  void save() {
    User user = EntityTestFactory.buildUser();
    Mockito.
        when(userRepository.save(user))
        .thenReturn(user);

    User actual = userService.save(user);

    Assertions.assertEquals(user, actual);
  }

  @Test
  void findByEmail_found() {
    User expected = EntityTestFactory.buildUser();
    Mockito
        .when(userRepository.findByEmail(expected.getEmail()))
        .thenReturn(Optional.of(expected));

    Optional<User> optionalUser = userService.findByEmail(expected.getEmail());

    optionalUser
        .ifPresentOrElse(user -> Assertions.assertEquals(expected, user),
            () -> Assertions.fail("user not found"));
  }

  @Test
  void findByEmail_notFound() {
    String email = RandomStringUtils.randomAlphabetic(20);
    Mockito
        .when(userRepository.findByEmail(email))
        .thenReturn(Optional.empty());

    Optional<User> optionalUser = userService.findByEmail(email);

    org.junit.jupiter.api.Assertions.assertThrows(AssertionError.class,
        () -> optionalUser.orElseThrow(Assertions.fail("user not found")));
  }

  @Test
  void register() {
    RegisterRequest request = DtoTestFactory.buildRegisterRequest();
    User user = EntityTestFactory.buildUser(request);
    JwtUser jwtUser = DtoTestFactory.buildJwtUser(user);
    Mockito
        .when(registerRequestUserMapper.map(request, User.class))
        .thenReturn(user);
    Mockito
        .when(passwordEncoder.encode(request.getPassword()))
        .thenReturn(user.getPassword());
    Mockito
        .when(userRepository.save(user))
        .thenReturn(user);
    Mockito
        .when(userJwtUserMapper.map(user, JwtUser.class))
        .thenReturn(jwtUser);

    JwtUser actual = userService.register(request);

    org.junit.jupiter.api.Assertions.assertEquals(jwtUser, actual);
  }

  @Test
  void subscribe() {
    User user = EntityTestFactory.buildUser();
    LocalDate expected = LocalDate.now().plusMonths(1);

    Mockito
        .when(userRepository.findByEmail(user.getEmail()))
        .thenReturn(Optional.of(user));
    Mockito
        .when(userRepository.save(user))
        .thenReturn(user);

    LocalDate actual = userService.subscribe(user.getEmail());

    org.junit.jupiter.api.Assertions.assertEquals(expected, actual);
  }

  @Test
  void subscribe_notFound() {
    String email = "admin@gmail.com";
    Mockito
        .when(userRepository.findByEmail(email))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(UsernameNotFoundException.class,
        () -> userService.subscribe(email));
  }
}