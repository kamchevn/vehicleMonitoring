package com.example.monitoringbackend.model.dto;

import com.example.monitoringbackend.model.*;
import com.example.monitoringbackend.model.enumerations.ComponentType;
import com.example.monitoringbackend.model.enumerations.Condition;
import com.example.monitoringbackend.model.enumerations.IntervalUnit;

import java.time.LocalDateTime;

public record DisplayComponentDto(
        Long id,
        String name,
        ComponentType type,
        Condition condition,
        IntervalUnit measuringUnitType,
        boolean warning,
        boolean checkRequired,
        LocalDateTime lastChecked
) {
    public static DisplayComponentDto from(Component component) {
        ComponentTemplate template = component.getTemplate();

        return new DisplayComponentDto(
                component.getId(),
                template.getName(),
                template.getComponentType(),
                component.getCondition(),
                template.getMeasuringUnitType(),
                component.isWarningFlag(),
                component.isNeedsCheck(),
                component.getLastChecked()
        );
    }
}
