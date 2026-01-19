package com.example.monitoringbackend.exceptions;

public class CheckNotFoundException extends RuntimeException{
    public CheckNotFoundException(Long id){
        super(String.format("Check with id %d not found.", id));
    }
}
