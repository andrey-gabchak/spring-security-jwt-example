package com.gabchak.example.security;

import static com.gabchak.example.constants.TestStaticModels.JWT_ADMIN;
import static com.gabchak.example.constants.TestStaticModels.USER;
import static org.mockito.Mockito.when;


import com.gabchak.example.dto.jwt.JwtUser;
import com.gabchak.example.dto.mapper.RegisterRequestUserMapper;
import com.gabchak.example.repositories.UserRepository;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class JwtUserDetailsServiceTest {

  @InjectMocks
  private JwtUserDetailsService jwtUserDetailsService;
  @Mock
  private UserRepository userRepository;
  @Mock
  private RegisterRequestUserMapper mapper;

  @Test
  void loadUserByUsername() {
    String email = "admin@gmail.com";
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(USER));
    when(mapper.map(USER, JwtUser.class)).thenReturn(JWT_ADMIN);
    UserDetails actual = jwtUserDetailsService.loadUserByUsername(email);
    Assertions.assertThat(JWT_ADMIN).isEqualToComparingOnlyGivenFields(actual,
        "username", "firstName", "lastName", "password");
  }

  @Test
  void loadUserByUsername_notFound() {
    String email = "admin@gmail.com";
    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
    Assertions.assertThatThrownBy(() ->
        jwtUserDetailsService.loadUserByUsername(email))
        .isInstanceOf(UsernameNotFoundException.class);
  }
}