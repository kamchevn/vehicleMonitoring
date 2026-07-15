package com.example.monitoringbackend.exceptions;

public class PasswordsDoNotMatchException extends RuntimeException {
  public PasswordsDoNotMatchException() {
    super("Passwords do not match.");
  }
}
