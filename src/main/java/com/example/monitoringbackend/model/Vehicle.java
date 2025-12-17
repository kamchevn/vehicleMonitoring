package com.example.monitoringbackend.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Vehicle {
    @Id
    private String internalCode;
    private String name;
    private Integer year;
    @Enumerated(EnumType.STRING)
    private Department department;
    private String type;
    @Enumerated(EnumType.STRING)
    private Condition condition;
    private String minCheckInterval;
    @Nullable
    private String maxCheckInterval;
    @Nullable
    private String counter;
    @Nullable
    private LocalDateTime lastInsertTime;
    @Nullable
    private LocalDateTime lastChecked;
    private boolean needsCheck = false;

    public Vehicle() {
    }

    public Vehicle(String internalCode, String name, Integer year, Department department, String type, Condition condition, String minCheckInterval, String maxCheckInterval, String counter, LocalDateTime lastInsertTime, LocalDateTime lastChecked) {
        this.internalCode = internalCode;
        this.name = name;
        this.year = year;
        this.department = department;
        this.type = type;
        this.condition = condition;
        this.minCheckInterval = minCheckInterval;
        this.maxCheckInterval = maxCheckInterval;
        this.counter = counter;
        this.lastInsertTime = lastInsertTime;
        this.lastChecked = lastChecked;
    }

    public String getInternalCode() {
        return internalCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public String getMinCheckInterval() {
        return minCheckInterval;
    }

    public void setMinCheckInterval(String minCheckInterval) {
        this.minCheckInterval = minCheckInterval;
    }

    public String getMaxCheckInterval() {
        return maxCheckInterval;
    }

    public void setMaxCheckInterval(String maxCheckInterval) {
        this.maxCheckInterval = maxCheckInterval;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public LocalDateTime getLastInsertTime() {
        return lastInsertTime;
    }

    public void setLastInsertTime(LocalDateTime lastInsertTime) {
        this.lastInsertTime = lastInsertTime;
    }

    public LocalDateTime getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(LocalDateTime lastChecked) {
        this.lastChecked = lastChecked;
    }

    public boolean isNeedsCheck() {
        return needsCheck;
    }

    public void setNeedsCheck(boolean needsCheck) {
        this.needsCheck = needsCheck;
    }
}
