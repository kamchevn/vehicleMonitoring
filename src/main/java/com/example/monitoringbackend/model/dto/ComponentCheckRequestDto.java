package com.example.monitoringbackend.model.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ComponentCheckRequestDto {
    private String checkType;
    private String note;
    private LocalDateTime checkTime;
    private List<ComponentCheckDetailDto> componentDetails;

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
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

    public List<ComponentCheckDetailDto> getComponentDetails() {
        return componentDetails;
    }

    public void setComponentDetails(List<ComponentCheckDetailDto> componentDetails) {
        this.componentDetails = componentDetails;
    }
}
