package com.gabchak.example.services;

import com.gabchak.example.dto.enums.Roles;
import com.gabchak.example.dto.jwt.RegisterRequest;
import com.gabchak.example.dto.mapper.RegisterRequestUserMapper;
import com.gabchak.example.dto.mapper.UserJwtUserMapper;
import com.gabchak.example.models.Role;
import com.gabchak.example.models.User;
import com.gabchak.example.repositories.UserRepository;
import com.gabchak.example.dto.jwt.JwtUser;
import com.gabchak.example.util.UserNotFoundExceptionMessageGenerator;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RegisterRequestUserMapper registerRequestUserMapper;
  private final UserJwtUserMapper userJwtUserMapper;

  /**
   * Saves a new user.
   *
   * @param user a new user data
   */
  public User save(User user) {
    return userRepository.save(user);
  }

  /**
   * Finds user by email.
   *
   * @param email user identifier
   * @return a new user if exists
   */
  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  /**
   * Registers a new user.
   *
   * @param request {@link RegisterRequest}
   * @return a new {@link User}
   */
  public JwtUser register(RegisterRequest request) {
    User user = registerRequestUserMapper.map(request, User.class);
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    User savedUser = userRepository.save(user);
    return userJwtUserMapper.map(savedUser, JwtUser.class);
  }

  /**
   * The method update subscription date
   * if it is not exist of expired.
   *
   * @param email user identifier
   * @return date of subscription
   */
  public LocalDate subscribe(String email)
      throws UsernameNotFoundException {
    Optional<User> byEmail = userRepository.findByEmail(email);
    return byEmail.map(user -> {
      LocalDate subscriptionEndDate = LocalDate.now().plusMonths(1);
      LocalDate currentDate = user.getSubscription();
      if (currentDate == null || currentDate.isBefore(subscriptionEndDate)) {
        HashSet<Role> roles = new HashSet<>(user.getRoles());
        roles.add(Role.fromEnum(Roles.PAID_USER));
        user.setRoles(roles);
        user.setSubscription(subscriptionEndDate);
        userRepository.save(user);
        return subscriptionEndDate;
      }
      return currentDate;
    }).orElseThrow(() ->
        new UsernameNotFoundException(
            UserNotFoundExceptionMessageGenerator.generateMessage(email)));
  }
}
