package com.example.monitoringbackend.model.dto;

import com.example.monitoringbackend.model.ComponentServiceDetail;
import com.example.monitoringbackend.model.enumerations.Condition;

public record DisplayServiceDetailDto(
        Long id,
        DisplayComponentDto component,
        Condition previousCondition,
        Condition currentCondition
) {
    public static DisplayServiceDetailDto from(ComponentServiceDetail detail) {
        return new DisplayServiceDetailDto(
                detail.getId(),
                DisplayComponentDto.from(detail.getComponent()),
                detail.getPreviousCondition(),
                detail.getCurrentCondition()
        );
    }
}
