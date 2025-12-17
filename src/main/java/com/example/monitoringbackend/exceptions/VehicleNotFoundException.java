package com.example.monitoringbackend.exceptions;

public class VehicleNotFoundException extends RuntimeException{
    public VehicleNotFoundException(String internalCode){
        super(String.format("Vehicle with internal code %s not found.",internalCode));
    }
}
