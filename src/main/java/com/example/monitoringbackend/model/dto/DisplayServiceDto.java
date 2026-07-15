package com.example.monitoringbackend.model.dto;

import com.example.monitoringbackend.model.ComponentService;
import com.example.monitoringbackend.model.enumerations.Condition;
import com.example.monitoringbackend.model.enumerations.ServiceType;
import java.time.LocalDateTime;
import java.util.List;

public record DisplayServiceDto(
    Long id,
    String note,
    LocalDateTime timeOfEntry,
    DisplayVehicleDto vehicle,
    Condition previousCondition,
    Condition currentCondition,
    ServiceType serviceType,
    List<DisplayServiceDetailDto> componentDetails) {
  public static DisplayServiceDto from(ComponentService service) {
    return new DisplayServiceDto(
        service.getId(),
        service.getNote(),
        service.getTimeOfEntry(),
        DisplayVehicleDto.from(service.getVehicle()),
        service.getPreviousCondition(),
        service.getCurrentCondition(),
        service.getServiceType(),
        service.getComponentDetails().stream().map(DisplayServiceDetailDto::from).toList());
  }
}
