package com.example.monitoringbackend.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer year;
    @Enumerated(EnumType.STRING)
    private VehicleType type;
    @Enumerated(EnumType.STRING)
    private Department department;
    @Enumerated(EnumType.STRING)
    private Condition condition;
    private boolean needsCheck = false;
    private boolean hasWarnings = false;

    public Vehicle() {
    }

    public Vehicle(String name, Integer year, Department department, VehicleType type, Condition condition) {
        this.name = name;
        this.year = year;
        this.department = department;
        this.type = type;
        this.condition = condition;
    }

    public Long getId() {
        return id;
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

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public boolean isNeedsCheck() {
        return needsCheck;
    }

    public void setNeedsCheck(boolean needsCheck) {
        this.needsCheck = needsCheck;
    }

    public boolean isHasWarnings() {
        return hasWarnings;
    }

    public void setHasWarnings(boolean hasWarnings) {
        this.hasWarnings = hasWarnings;
    }
}
