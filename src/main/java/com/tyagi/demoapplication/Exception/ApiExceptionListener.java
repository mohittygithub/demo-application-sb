package com.tyagi.demoapplication.Exception;

import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ApiExceptionListener {

  // ResourceNotFoundException
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> resourceNotFoundException(
    ResourceNotFoundException ex,
    WebRequest request
  ) {
    ErrorResponse response = new ErrorResponse(
      HttpStatus.NOT_FOUND.value(),
      new Date(),
      ex.getMessage(),
      request.getDescription(false)
    );

    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  // BadRequestException
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> badRequestException(
          BadRequestException ex,
          WebRequest request
  ) {
    ErrorResponse response = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            new Date(),
            ex.getMessage(),
            request.getDescription(false)
    );

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  // InvalidJwtTokenException,UnauthorizedAccessException
  @ExceptionHandler({ InvalidJwtTokenException.class })
  public ResponseEntity<ErrorResponse> invalidJwtTokenException(
    InvalidJwtTokenException ex,
    WebRequest request
  ) {
    ErrorResponse response = new ErrorResponse(
      HttpStatus.NOT_FOUND.value(),
      new Date(),
      ex.getMessage(),
      request.getDescription(false)
    );

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
}
