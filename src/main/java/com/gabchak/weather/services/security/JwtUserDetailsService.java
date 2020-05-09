package com.gabchak.weather.services.security;

import com.gabchak.weather.repositories.UserRepository;
import com.gabchak.weather.services.security.jwt.JwtUser;
import com.gabchak.weather.services.security.jwt.JwtUserFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Autowired
  public JwtUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByEmail(username)
        .map(user -> {
          JwtUser jwtUser = JwtUserFactory.create(user);
          log.info("IN loadUserByUsername - user with username: {} successfully loaded", username);
          return jwtUser;
        }).orElseThrow(() ->
            new UsernameNotFoundException("User with username: " + username + " not found"));
  }
}
