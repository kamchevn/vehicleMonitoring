package com.example.monitoringbackend.exceptions;

public class InsertDateTooLateException extends RuntimeException{
    public InsertDateTooLateException(){
        super("You inserted the interval for this vehicle too late!");
    }
}
