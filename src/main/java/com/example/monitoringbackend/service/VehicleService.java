package com.example.monitoringbackend.service;

import com.example.monitoringbackend.model.Vehicle;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface VehicleService {
    Vehicle findById(Long id);
    Page<Vehicle> findPage(String name, String type, Integer pageNum, Integer pageSize);
    List<Vehicle> findAll();
    Vehicle createNewVehicle(String name, int year, String department, String type, String condition);
    Vehicle editVehicle(Long id, String name, int year, String department, String type, String condition);
    Vehicle deleteVehicle(Long id);
    void insertDistanceOrHoursForVehicle(Long id, String information, LocalDateTime insertTime);
    void changeVehicleCondition(Long id);

}
