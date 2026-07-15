package com.example.monitoringbackend.model;

import com.example.monitoringbackend.model.enumerations.Condition;
import com.example.monitoringbackend.model.enumerations.ServiceType;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ComponentService {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Nullable private String note;
  private LocalDateTime timeOfEntry;

  @ManyToOne(fetch = FetchType.LAZY)
  private Vehicle vehicle;

  private Condition previousCondition;
  private Condition currentCondition;
  private ServiceType serviceType;

  @OneToMany(mappedBy = "componentService", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ComponentServiceDetail> componentDetails = new ArrayList<>();

  public ComponentService() {}

  public ComponentService(
      String note,
      LocalDateTime timeOfEntry,
      Vehicle vehicle,
      Condition previousCondition,
      Condition currentCondition,
      ServiceType serviceType) {
    this.note = note;
    this.timeOfEntry = timeOfEntry;
    this.vehicle = vehicle;
    this.previousCondition = previousCondition;
    this.currentCondition = currentCondition;
    this.serviceType = serviceType;
  }

  public Long getId() {
    return id;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
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

  public ServiceType getServiceType() {
    return serviceType;
  }

  public void setServiceType(ServiceType serviceType) {
    this.serviceType = serviceType;
  }

  public List<ComponentServiceDetail> getComponentDetails() {
    return componentDetails;
  }

  public void setComponentDetails(List<ComponentServiceDetail> componentDetails) {
    this.componentDetails = componentDetails;
  }

  public void addComponentDetail(ComponentServiceDetail detail) {
    detail.setComponentCheck(this);
    this.componentDetails.add(detail);
  }

  public void removeComponentDetail(ComponentServiceDetail detail) {
    detail.setComponentCheck(null);
    this.componentDetails.remove(detail);
  }
}
