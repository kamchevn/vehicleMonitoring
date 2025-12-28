package com.example.monitoringbackend.service;

import com.example.monitoringbackend.model.ComponentTemplate;
import com.example.monitoringbackend.model.VehicleType;

import java.util.List;

public interface ComponentTemplateService {
    ComponentTemplate findById(Long id);
    List<ComponentTemplate> getTemplatesForVehicle(VehicleType vehicleType);
    ComponentTemplate updateRules(Long id, Integer minCheckInterval, Integer warningInterval);
}
