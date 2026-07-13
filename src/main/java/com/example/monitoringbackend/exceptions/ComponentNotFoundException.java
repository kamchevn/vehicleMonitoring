package com.example.monitoringbackend.exceptions;

public class ComponentNotFoundException extends RuntimeException {
  public ComponentNotFoundException(Long id) {
    super(String.format("Component with id %d not found.", id));
  }
}
