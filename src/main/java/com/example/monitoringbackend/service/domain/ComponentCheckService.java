package com.example.monitoringbackend.service.domain;

import com.example.monitoringbackend.model.Component;
import com.example.monitoringbackend.model.ComponentCheck;
import com.example.monitoringbackend.model.ComponentCheckDetail;
import com.example.monitoringbackend.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;

public interface ComponentCheckService {
    ComponentCheck findById(Long checkId);
    Page<ComponentCheck> findPage(UserDetails user,Long vehicleId, String checkType, Integer pageNum, Integer pageSize);
    void checkConditionForVehicle(Long id, String checkType, String note, LocalDateTime checkTime, List<ComponentCheckDetail> componentDetails);
}
