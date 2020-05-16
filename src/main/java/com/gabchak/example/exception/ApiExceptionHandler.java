package com.gabchak.example.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.time.ZonedDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(value = {JwtException.class,
      UsernameNotFoundException.class,
      BadCredentialsException.class,
      ExpiredJwtException.class,
      UnsupportedJwtException.class,
      MalformedJwtException.class,
      SignatureException.class,
      IllegalArgumentException.class})
  public ResponseEntity<ApiExceptionResponse> handleJwtAuthenticationException(RuntimeException e) {
    ApiExceptionResponse exceptionResponse = new ApiExceptionResponse(
        e.getMessage(),
        HttpStatus.BAD_REQUEST,
        ZonedDateTime.now());
    return ResponseEntity
        .badRequest()
        .body(exceptionResponse);
  }
}
