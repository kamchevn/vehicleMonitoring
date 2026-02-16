package com.example.monitoringbackend.model;

import com.example.monitoringbackend.model.enumerations.Condition;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class ComponentServiceDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "component_service_id")
    private ComponentService componentService;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id")
    private Component component;

    private Condition previousCondition;
    private Condition currentCondition;

    public ComponentServiceDetail() {}

    public ComponentServiceDetail(ComponentService componentService, Component component, Condition previousCondition, Condition currentCondition) {
        this.componentService = componentService;
        this.component = component;
        this.previousCondition = previousCondition;
        this.currentCondition = currentCondition;
    }

    public Long getId() {
        return id;
    }

    public ComponentService getComponentCheck() {
        return componentService;
    }

    public void setComponentCheck(ComponentService componentService) {
        this.componentService = componentService;
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
