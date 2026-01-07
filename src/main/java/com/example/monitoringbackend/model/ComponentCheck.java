package com.example.monitoringbackend.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ComponentCheck {
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

    @OneToMany(mappedBy = "componentCheck", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComponentCheckDetail> componentDetails = new ArrayList<>();

    public ComponentCheck(){
    }

    public ComponentCheck(String note, LocalDateTime timeOfEntry, Vehicle vehicle, Condition previousCondition, Condition currentCondition, ConditionCheckType checkType) {
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

    public List<ComponentCheckDetail> getComponentDetails() { return componentDetails; }
    public void setComponentDetails(List<ComponentCheckDetail> componentDetails) { this.componentDetails = componentDetails; }

    public void addComponentDetail(ComponentCheckDetail detail) {
        detail.setComponentCheck(this);
        this.componentDetails.add(detail);
    }
}
