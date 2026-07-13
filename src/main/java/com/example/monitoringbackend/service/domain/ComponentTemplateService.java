package com.example.monitoringbackend.service.domain;

import com.example.monitoringbackend.model.ComponentTemplate;
import com.example.monitoringbackend.model.enumerations.ComponentType;
import com.example.monitoringbackend.model.enumerations.VehicleType;
import java.util.List;

public interface ComponentTemplateService {
  ComponentTemplate findById(Long id);

  List<ComponentTemplate> getTemplatesForVehicleType(VehicleType vehicleType);

  List<ComponentTemplate> getTemplatesForVehicleTypeAndComponentType(
      VehicleType vehicleType, ComponentType componentType);

  ComponentTemplate updateRules(Long id, Integer minCheckInterval, Integer warningInterval);
}
