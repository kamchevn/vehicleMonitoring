package com.example.monitoringbackend.exceptions;

public class InvalidConfirmationTokenException extends RuntimeException {
  public InvalidConfirmationTokenException() {
    super("Confirmation token is invalid or has expired.");
  }
}
