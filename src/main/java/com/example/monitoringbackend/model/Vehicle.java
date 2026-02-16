package com.example.monitoringbackend.model;

import com.example.monitoringbackend.model.dto.InsertWindowStatusDto;
import com.example.monitoringbackend.model.enumerations.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Integer totalKilometers;
    @Enumerated(EnumType.STRING)
    private VehicleType type;
    @Nullable
    @Enumerated(EnumType.STRING)
    private VehicleFuelType fuelType;
    @Nullable
    @Enumerated(EnumType.STRING)
    private CoolingType coolingType;
    @Nullable
    @Enumerated(EnumType.STRING)
    private DrivenType drivenType;
    @Enumerated(EnumType.STRING)
    private Condition condition;
    private boolean needsCheck = false;
    private boolean hasWarnings = false;

    @Enumerated(EnumType.STRING)
    private IntervalInsertPeriod insertPeriodType;

    private LocalDateTime lastInsertKilometers = null;
    private LocalDateTime lastInsertFuel = null;

    @Transient
    private InsertWindowStatusDto insertWindowStatus;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private User user;

    private boolean penaltyActive = false;

    public Vehicle() {
    }

    public Vehicle(String name, Integer year, Integer totalKilometers, VehicleType type, VehicleFuelType fuelType, CoolingType coolingType, DrivenType drivenType, Condition condition, IntervalInsertPeriod insertPeriodType, User user) {
        this.name = name;
        this.year = year;
        this.totalKilometers = totalKilometers;
        this.type = type;
        this.fuelType = fuelType;
        this.coolingType = coolingType;
        this.drivenType = drivenType;
        this.condition = condition;
        this.insertPeriodType = insertPeriodType;
        this.user = user;
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

    public Integer getTotalKilometers() {
        return totalKilometers;
    }

    public void setTotalKilometers(Integer totalKilometers) {
        this.totalKilometers = totalKilometers;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public VehicleFuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(VehicleFuelType fuelType) {
        this.fuelType = fuelType;
    }

    public CoolingType getCoolingType() {
        return coolingType;
    }

    public void setCoolingType(CoolingType coolingType) {
        this.coolingType = coolingType;
    }

    public DrivenType getDrivenType() {
        return drivenType;
    }

    public void setDrivenType(DrivenType drivenType) {
        this.drivenType = drivenType;
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

    public IntervalInsertPeriod getInsertPeriodType() {
        return insertPeriodType;
    }

    public void setInsertPeriodType(IntervalInsertPeriod insertPeriodType) {
        this.insertPeriodType = insertPeriodType;
    }

    public LocalDateTime getLastInsertKilometers() {
        return lastInsertKilometers;
    }

    public void setLastInsertKilometers(LocalDateTime lastInsertKilometers) {
        this.lastInsertKilometers = lastInsertKilometers;
    }

    public LocalDateTime getLastInsertFuel() {
        return lastInsertFuel;
    }

    public void setLastInsertFuel(LocalDateTime lastInsertFuel) {
        this.lastInsertFuel = lastInsertFuel;
    }

    public InsertWindowStatusDto getInsertWindowStatus() {
        return insertWindowStatus;
    }

    public void setInsertWindowStatus(InsertWindowStatusDto insertWindowStatus) {
        this.insertWindowStatus = insertWindowStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isPenaltyActive() {
        return penaltyActive;
    }

    public void setPenaltyActive(boolean penaltyActive) {
        this.penaltyActive = penaltyActive;
    }
}
