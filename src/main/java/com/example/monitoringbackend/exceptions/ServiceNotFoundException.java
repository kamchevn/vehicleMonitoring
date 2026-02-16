package com.example.monitoringbackend.exceptions;

public class ServiceNotFoundException extends RuntimeException{
    public ServiceNotFoundException(Long id){
        super(String.format("Service with id %d not found.", id));
    }
}
