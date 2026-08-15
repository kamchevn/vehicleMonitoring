package com.example.monitoringbackend.web.handlers;

import com.example.monitoringbackend.exceptions.*;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
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

  @ExceptionHandler(EmailAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handleEmailExists(EmailAlreadyExistsException ex) {
    return Map.of("message", ex.getMessage());
  }

  @ExceptionHandler(InvalidPasswordFormatException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handleInvalidPasswordFormat(InvalidPasswordFormatException ex) {
    return Map.of("message", ex.getMessage());
  }

  @ExceptionHandler(PasswordsDoNotMatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handlePasswordsDoNotMatch(PasswordsDoNotMatchException ex) {
    return Map.of("message", ex.getMessage());
  }

  @ExceptionHandler(InvalidConfirmationTokenException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handleInvalidConfirmationToken(InvalidConfirmationTokenException ex) {
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

  @ExceptionHandler(AccountNotEnabledException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public Map<String, String> handleAccountNotEnabled(AccountNotEnabledException ex) {
    return Map.of("message", ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handleValidation(MethodArgumentNotValidException ex) {
    String message = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
    return Map.of("message", message);
  }
  
  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public Map<String, String> handleAccessDenied(AccessDeniedException ex) {
    return Map.of("message", "You do not have permission to access this resource.");
  }

  @ExceptionHandler({
    VehicleNotFoundException.class,
    ComponentNotFoundException.class,
    ServiceNotFoundException.class
  })
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Map<String, String> handleNotFound(RuntimeException ex) {
    return Map.of("message", ex.getMessage());
  }
}
