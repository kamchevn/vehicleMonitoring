package com.example.monitoringbackend.model;

import com.example.monitoringbackend.model.enumerations.IntervalUnit;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class IntervalInsert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime timeOfEntry;
    @ManyToOne
    private Vehicle vehicle;
    @Enumerated(EnumType.STRING)
    private IntervalUnit unit;
    private Integer amount;
    private boolean late = false;

    public IntervalInsert() {
    }

    public IntervalInsert(LocalDateTime timeOfEntry, Vehicle vehicle, IntervalUnit unit, Integer amount, boolean late) {
        this.timeOfEntry = timeOfEntry;
        this.vehicle = vehicle;
        this.unit = unit;
        this.amount = amount;
        this.late = late;
    }

    public Long getId() {
        return id;
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

    public IntervalUnit getUnit() {
        return unit;
    }

    public void setUnit(IntervalUnit unit) {
        this.unit = unit;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public boolean isLate() {
        return late;
    }

    public void setLate(boolean late) {
        this.late = late;
    }
}
