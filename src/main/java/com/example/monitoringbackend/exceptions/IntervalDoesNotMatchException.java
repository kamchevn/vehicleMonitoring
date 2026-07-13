package com.example.monitoringbackend.exceptions;

public class IntervalDoesNotMatchException extends RuntimeException {
  public IntervalDoesNotMatchException() {
    super(
        "The unit type of the inserted information doesn't match with the unit type of the counter for this vehicle!");
  }
}
