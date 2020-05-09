package com.gabchak.example.services.security;

import static com.gabchak.example.constants.TestStaticModels.JWT_USER;
import static com.gabchak.example.constants.TestStaticModels.USER;
import static org.mockito.Mockito.when;


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

  @Test
  void loadUserByUsername() {
    String email = "admin@gmail.com";
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(USER));
    UserDetails actual = jwtUserDetailsService.loadUserByUsername(email);
    Assertions.assertThat(JWT_USER).isEqualToComparingOnlyGivenFields(actual,
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