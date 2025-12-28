package com.example.monitoringbackend.service;

import com.example.monitoringbackend.model.Component;
import com.example.monitoringbackend.model.ComponentCheck;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface ComponentCheckService {
    Page<ComponentCheck> findPage(Long vehicleId, String checkType, Integer pageNum, Integer pageSize);
    void checkConditionForVehicle(Long id, String checkType, String note, LocalDateTime checkTime, List<Component> componentsChecked);
}
