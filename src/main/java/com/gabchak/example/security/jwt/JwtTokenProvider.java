package com.gabchak.example.security.jwt;

import com.gabchak.example.dto.jwt.AuthResponse;
import com.gabchak.example.security.JwtUserDetailsService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

  private String secret;
  private final long validityInMilliseconds;
  private final String authHeaderPrefix;
  private final UserDetailsService userDetailsService;

  /**
   * Constructor of the class.
   *
   * @param authHeaderPrefix       prefix for Authorization header
   * @param secret                 secret word to encode token
   * @param userDetailsService     {@link JwtUserDetailsService}
   * @param validityInMilliseconds how long is token not expire
   */
  public JwtTokenProvider(@Value("${jwt.token.secret}") String secret,
                          @Value("${jwt.token.expired}") long validityInMilliseconds,
                          @Value("${jwt.token.header.prefix}") String authHeaderPrefix,
                          UserDetailsService userDetailsService) {
    this.secret = secret;
    this.validityInMilliseconds = validityInMilliseconds;
    this.authHeaderPrefix = authHeaderPrefix;
    this.userDetailsService = userDetailsService;
  }

  @PostConstruct
  protected void init() {
    secret = Base64.getEncoder().encodeToString(secret.getBytes());
  }

  /**
   * Builds {@link AuthResponse} from {@link UserDetails}.
   *
   * @param userDetails user details
   * @return {@link AuthResponse}
   */
  public AuthResponse buildAuthResponse(UserDetails userDetails) {
    List<String> authorities = getAuthorityNames(userDetails.getAuthorities());
    String token = createToken(userDetails.getUsername(), authorities);
    return new AuthResponse(token, userDetails.getUsername(), authorities);
  }

  /**
   * Builds list of role names from user roles.
   *
   * @param userRoles collection of user roles
   * @return list of user role names
   */
  private List<String> getAuthorityNames(Collection<? extends GrantedAuthority> userRoles) {
    return userRoles.stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());
  }

  /**
   * Creates token from username and authorities.
   *
   * @param username    user identifier
   * @param authorities list of user authorities
   * @return token
   */
  public String createToken(String username, List<String> authorities) {

    Claims claims = Jwts.claims().setSubject(username);
    claims.put("authorities", authorities);

    Date now = new Date();
    Date validity = new Date(now.getTime() + validityInMilliseconds);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(SignatureAlgorithm.HS256, secret)
        .compact();
  }

  /**
   * Authenticate user by token.
   *
   * @param token user identifier
   * @return user authentication
   */
  public Authentication getAuthentication(String token) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
    return new UsernamePasswordAuthenticationToken(userDetails,
        "", userDetails.getAuthorities());
  }

  /**
   * Finds username by token.
   *
   * @param token user identifier
   * @return username
   */
  public String getUsername(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
  }

  /**
   * Resolves token from request headers.
   *
   * @param req request
   * @return token
   */
  public String resolveToken(HttpServletRequest req) {
    String authorization = req.getHeader(HttpHeaders.AUTHORIZATION);
    return authorization != null && authorization.startsWith(authHeaderPrefix)
        ? authorization.substring(authHeaderPrefix.length()) : null;
  }

  /**
   * Checks if token is valid.
   *
   * @param token user identifier
   * @return is valid
   */
  public boolean validateToken(String token) {
    try {
      Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
      return !claims.getBody().getExpiration().before(new Date());
    } catch (JwtException | IllegalArgumentException e) {
      throw new JwtAuthenticationException("JWT token is expired or invalid");
    }
  }
}
