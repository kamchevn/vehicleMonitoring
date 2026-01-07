package com.example.monitoringbackend.model;

import jakarta.persistence.*;

@Entity
public class ComponentTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @Enumerated(EnumType.STRING)
    private ComponentType componentType;

    @Enumerated(EnumType.STRING)
    private IntervalUnit measuringUnitType;

    private Integer minCheckInterval;
    private Integer warningInterval;

    public ComponentTemplate() {
    }

    public ComponentTemplate(String name, VehicleType vehicleType, ComponentType componentType, IntervalUnit measuringUnitType, Integer minCheckInterval, Integer warningInterval) {
        this.name = name;
        this.vehicleType = vehicleType;
        this.componentType = componentType;
        this.measuringUnitType = measuringUnitType;
        this.minCheckInterval = minCheckInterval;
        this.warningInterval = warningInterval;
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

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }

    public IntervalUnit getMeasuringUnitType() {
        return measuringUnitType;
    }

    public void setMeasuringUnitType(IntervalUnit measuringUnitType) {
        this.measuringUnitType = measuringUnitType;
    }

    public Integer getMinCheckInterval() {
        return minCheckInterval;
    }

    public void setMinCheckInterval(Integer minCheckInterval) {
        this.minCheckInterval = minCheckInterval;
    }

    public Integer getWarningInterval() {
        return warningInterval;
    }

    public void setWarningInterval(Integer warningInterval) {
        this.warningInterval = warningInterval;
    }
}
