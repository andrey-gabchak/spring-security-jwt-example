package com.gabchak.example.security;

import com.gabchak.example.dto.jwt.JwtUser;
import com.gabchak.example.dto.mapper.UserJwtUserMapper;
import com.gabchak.example.models.User;
import com.gabchak.example.repositories.UserRepository;
import com.gabchak.example.util.UserNotFoundExceptionMessageGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;
  private final UserJwtUserMapper mapper;

  /**
   * Originally the method should find user by username
   * but because I use email instead of username in {@link User}
   * the method finds user by email.
   *
   * Cause of overriding method of UserDetailsService
   * the method must have the same name.
   * */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByEmail(username)
        .map(user -> {
          JwtUser jwtUser = mapper.map(user, JwtUser.class);
          log.info("IN loadUserByUsername - user with username: {} successfully loaded", username);
          return jwtUser;
        }).orElseThrow(() ->
            new UsernameNotFoundException(
                UserNotFoundExceptionMessageGenerator.generateMessage(username)));
  }
}
