package com.example.monitoringbackend.service;

import com.example.monitoringbackend.model.ConditionCheck;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface ConditionCheckService {
    Page<ConditionCheck> findPage(String vehicleInternalCode, String checkType, Integer pageNum, Integer pageSize);

}
