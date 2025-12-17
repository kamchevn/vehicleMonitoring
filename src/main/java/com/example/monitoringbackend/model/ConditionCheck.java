package com.example.monitoringbackend.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class ConditionCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Nullable
    private String note;
    private LocalDateTime timeOfEntry;
    @ManyToOne
    private Vehicle vehicle;
    private Condition previousCondition;
    private Condition currentCondition;
    private ConditionCheckType checkType;

    public ConditionCheck(){

    }

    public ConditionCheck(String note, LocalDateTime timeOfEntry, Vehicle vehicle, Condition previousCondition, Condition currentCondition, ConditionCheckType checkType) {
        this.note = note;
        this.timeOfEntry = timeOfEntry;
        this.vehicle = vehicle;
        this.previousCondition = previousCondition;
        this.currentCondition = currentCondition;
        this.checkType = checkType;
    }

    public Long getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getTimeOfEntry() {
        return timeOfEntry;
    }

    public void setTimeOfEntry(LocalDateTime timeOfEntry) {
        this.timeOfEntry = timeOfEntry;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Condition getPreviousCondition() {
        return previousCondition;
    }

    public void setPreviousCondition(Condition previousCondition) {
        this.previousCondition = previousCondition;
    }

    public Condition getCurrentCondition() {
        return currentCondition;
    }

    public void setCurrentCondition(Condition currentCondition) {
        this.currentCondition = currentCondition;
    }

    public ConditionCheckType getCheckType() {
        return checkType;
    }

    public void setCheckType(ConditionCheckType checkType) {
        this.checkType = checkType;
    }
}
