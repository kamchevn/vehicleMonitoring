package com.example.monitoringbackend.model.dto;

import com.example.monitoringbackend.model.*;

public record DisplayComponentDto(
        Long id,
        ComponentType type,
        Condition condition,
        IntervalUnit measuringUnitType,
        boolean warning,
        boolean checkRequired
) {
    public static DisplayComponentDto from(Component component) {
        ComponentTemplate template = component.getTemplate();

        return new DisplayComponentDto(
                component.getId(),
                template.getComponentType(),
                component.getCondition(),
                template.getMeasuringUnitType(),
                component.isWarningFlag(),
                component.isNeedsCheck()
        );
    }
}
