package com.example.monitoringbackend.service.impl;

import com.example.monitoringbackend.model.ComponentTemplate;
import com.example.monitoringbackend.model.VehicleType;
import com.example.monitoringbackend.repository.ComponentTemplateRepository;
import com.example.monitoringbackend.service.ComponentTemplateService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComponentTemplateServiceImpl implements ComponentTemplateService {
    private final ComponentTemplateRepository repository;

    public ComponentTemplateServiceImpl(ComponentTemplateRepository repository) {
        this.repository = repository;
    }

    @Override
    public ComponentTemplate findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Template not found"));
    }

    @Override
    public List<ComponentTemplate> getTemplatesForVehicle(VehicleType vehicleType) {
        return repository.findAllByVehicleType(vehicleType);
    }

    @Override
    public ComponentTemplate updateRules(
            Long id,
            Integer minCheckInterval,
            Integer warningInterval
    ) {
        ComponentTemplate template = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Template not found"));

        if (warningInterval >= minCheckInterval) {
            throw new IllegalArgumentException(
                    "Warning interval must be less than minCheckInterval"
            );
        }

        template.setMinCheckInterval(minCheckInterval);
        template.setWarningInterval(warningInterval);

        return repository.save(template);
    }
}
