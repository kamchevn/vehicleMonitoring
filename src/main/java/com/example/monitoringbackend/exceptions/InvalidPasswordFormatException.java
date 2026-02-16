package com.example.monitoringbackend.exceptions;

public class InvalidPasswordFormatException extends RuntimeException{
    public InvalidPasswordFormatException() {
        super("Password must include 1 uppercase letter, 1 number and 1 symbol.");
    }
}
