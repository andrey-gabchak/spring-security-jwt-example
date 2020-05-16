package com.gabchak.example.rest;


import com.gabchak.example.dto.jwt.AuthResponse;
import com.gabchak.example.dto.jwt.JwtUser;
import com.gabchak.example.dto.jwt.LoginRequest;
import com.gabchak.example.dto.jwt.RegisterRequest;
import com.gabchak.example.security.jwt.JwtTokenProvider;
import com.gabchak.example.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
  public static final String LOGIN = "/login";
  public static final String REGISTER = "/register";
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserDetailsService userDetailsService;
  private final UserService userService;

  /**
   * The method login an existing user.
   *
   * @param request {@link LoginRequest}
   * @return {@link AuthResponse}
   */
  @PostMapping(LOGIN)
  public ResponseEntity<AuthResponse> login(
      @Validated @RequestBody LoginRequest request) {
      String username = request.getEmail();
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              username, request.getPassword()));
      UserDetails user = userDetailsService.loadUserByUsername(username);
      return ResponseEntity.ok(jwtTokenProvider.buildAuthResponse(user));
  }

  /**
   * The method register a new user.
   *
   * @param request {@link RegisterRequest}
   * @return {@link AuthResponse}
   */
  @PostMapping(REGISTER)
  public ResponseEntity<AuthResponse> register(
      @Validated @RequestBody RegisterRequest request) {
    JwtUser registeredUser = userService.register(request);
    return ResponseEntity
        .ok(jwtTokenProvider.buildAuthResponse(registeredUser));
  }

}