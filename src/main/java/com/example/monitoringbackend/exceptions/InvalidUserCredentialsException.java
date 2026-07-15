package com.example.monitoringbackend.exceptions;

public class InvalidUserCredentialsException extends RuntimeException {
  public InvalidUserCredentialsException() {
    super("Invalid username or password.");
  }
}
