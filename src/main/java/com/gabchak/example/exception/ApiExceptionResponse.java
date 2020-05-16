package com.gabchak.example.exception;

import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ApiExceptionResponse {
  private final String message;
  private final HttpStatus httpStatus;
  private final ZonedDateTime timestamp;
}
