package com.example.monitoringbackend.service;

import com.example.monitoringbackend.model.ComponentTemplate;
import com.example.monitoringbackend.model.ComponentType;
import com.example.monitoringbackend.model.VehicleType;

import java.util.List;

public interface ComponentTemplateService {
    ComponentTemplate findById(Long id);
    List<ComponentTemplate> getTemplatesForVehicleType(VehicleType vehicleType);
    List<ComponentTemplate> getTemplatesForVehicleTypeAndComponentType(VehicleType vehicleType, ComponentType componentType);
    ComponentTemplate updateRules(Long id, Integer minCheckInterval, Integer warningInterval);
}
