package com.example.monitoringbackend.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Component {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private ComponentTemplate template;
    @ManyToOne(fetch = FetchType.LAZY)
    private Vehicle vehicle;
    @Enumerated(EnumType.STRING)
    private Condition condition;
    private Integer counter;
    @Nullable
    private LocalDateTime lastInsertTime = null;
    @Nullable
    private LocalDateTime lastChecked = null;

    @OneToMany(mappedBy = "component")
    private List<ComponentCheckDetail> componentCheckDetails = new ArrayList<>();

    private boolean warningFlag = false;
    private boolean needsCheck = false;

    public Component() {
    }

    public Component(Vehicle vehicle, ComponentTemplate componentTemplate, Condition condition, Integer counter) {
        this.vehicle = vehicle;
        this.template = componentTemplate;
        this.condition = condition;
        this.counter = counter;
    }

    public Long getId() {
        return id;
    }

    public ComponentTemplate getTemplate() {
        return template;
    }

    public void setTemplate(ComponentTemplate template) {
        this.template = template;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }
    public Integer getCounter() {
        return counter;
    }
    public void setCounter(Integer counter) {
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

    public boolean isWarningFlag() {
        return warningFlag;
    }

    public void setWarningFlag(boolean warningFlag) {
        this.warningFlag = warningFlag;
    }

    public boolean isNeedsCheck() {
        return needsCheck;
    }

    public void setNeedsCheck(boolean needsCheck) {
        this.needsCheck = needsCheck;
    }

    public List<ComponentCheckDetail> getComponentCheckDetails() {
        return componentCheckDetails;
    }

    public void setComponentCheckDetails(List<ComponentCheckDetail> componentCheckDetails) {
        this.componentCheckDetails = componentCheckDetails;
    }
}
