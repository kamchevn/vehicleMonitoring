package com.example.monitoringbackend.web.handlers;

import com.example.monitoringbackend.exceptions.*;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UsernameAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handleUsernameExists(UsernameAlreadyExistsException ex) {
    return Map.of("message", ex.getMessage());
  }

  @ExceptionHandler(InvalidPasswordFormatException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handleInvalidPasswordFormat(InvalidPasswordFormatException ex) {
    return Map.of("message", ex.getMessage());
  }

  @ExceptionHandler(PasswordsDoNotMatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handleInvalidPasswordFormat(PasswordsDoNotMatchException ex) {
    return Map.of("message", ex.getMessage());
  }

  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public Map<String, String> handleUserNotFound(UserNotFoundException ex) {
    return Map.of("message", ex.getMessage());
  }

  @ExceptionHandler(InvalidUserCredentialsException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public Map<String, String> handleInvalidCredentials(InvalidUserCredentialsException ex) {
    return Map.of("message", ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handleValidation(MethodArgumentNotValidException ex) {
    String message = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
    return Map.of("message", message);
  }
}
