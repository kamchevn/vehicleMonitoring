package com.example.monitoringbackend.model.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ComponentServiceRequestDto {
  private String serviceType;
  private String note;
  private LocalDateTime checkTime;
  private List<ComponentServiceDetailDto> componentDetails;

  public String getServiceType() {
    return serviceType;
  }

  public void setServiceType(String serviceType) {
    this.serviceType = serviceType;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public LocalDateTime getCheckTime() {
    return checkTime;
  }

  public void setCheckTime(LocalDateTime checkTime) {
    this.checkTime = checkTime;
  }

  public List<ComponentServiceDetailDto> getComponentDetails() {
    return componentDetails;
  }

  public void setComponentDetails(List<ComponentServiceDetailDto> componentDetails) {
    this.componentDetails = componentDetails;
  }
}
