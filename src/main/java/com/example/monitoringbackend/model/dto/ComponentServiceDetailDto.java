package com.example.monitoringbackend.model.dto;

import com.example.monitoringbackend.model.enumerations.Condition;

public class ComponentServiceDetailDto {
  private Long componentId;
  private Condition currentCondition;

  public Long getComponentId() {
    return componentId;
  }

  public void setComponentId(Long componentId) {
    this.componentId = componentId;
  }

  public Condition getCurrentCondition() {
    return currentCondition;
  }

  public void setCurrentCondition(Condition currentCondition) {
    this.currentCondition = currentCondition;
  }
}
