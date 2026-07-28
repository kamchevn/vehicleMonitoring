package com.example.monitoringbackend.exceptions;

public class AccountNotEnabledException extends RuntimeException {
  public AccountNotEnabledException() {
    super("Account is not enabled. Please confirm your email first.");
  }
}
