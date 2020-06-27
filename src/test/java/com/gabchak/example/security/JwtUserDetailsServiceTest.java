package com.gabchak.example.security;

import com.gabchak.example.DtoTestFactory;
import com.gabchak.example.EntityTestFactory;
import com.gabchak.example.dto.jwt.JwtUser;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class JwtUserDetailsServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private UserJwtUserMapper mapper;
  @InjectMocks
  private JwtUserDetailsService jwtUserDetailsService;

  @Test
  void loadUserByUsername() {
    User user = EntityTestFactory.buildUser();
    JwtUser expected = DtoTestFactory.buildJwtUser(user);

    Mockito
        .when(userRepository.findByEmail(user.getEmail()))
        .thenReturn(Optional.of(user));
    Mockito
        .when(mapper.map(user, JwtUser.class))
        .thenReturn(expected);

    UserDetails actual = jwtUserDetailsService.loadUserByUsername(user.getEmail());
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void loadUserByUsername_notFound() {
    String email = RandomStringUtils.randomAlphabetic(20);
    Mockito
        .when(userRepository.findByEmail(email))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(UsernameNotFoundException.class,
        () -> jwtUserDetailsService.loadUserByUsername(email));
  }
}