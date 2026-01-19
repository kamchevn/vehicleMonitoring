package com.example.monitoringbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class ComponentCheckDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "component_check_id")
    private ComponentCheck componentCheck;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id")
    private Component component;

    private Condition previousCondition;
    private Condition currentCondition;

    public ComponentCheckDetail() {}

    public ComponentCheckDetail(ComponentCheck componentCheck, Component component, Condition previousCondition, Condition currentCondition) {
        this.componentCheck = componentCheck;
        this.component = component;
        this.previousCondition = previousCondition;
        this.currentCondition = currentCondition;
    }

    public Long getId() {
        return id;
    }

    public ComponentCheck getComponentCheck() {
        return componentCheck;
    }

    public void setComponentCheck(ComponentCheck componentCheck) {
        this.componentCheck = componentCheck;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
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
}
