package com.example.monitoringbackend.service;

import com.example.monitoringbackend.model.Vehicle;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface VehicleService {
    Vehicle getVehicleByInternalCode(String internalCode);
    Page<Vehicle> findPage(String department, String type, Integer pageNum, Integer pageSize);
    List<Vehicle> findAll();
    List<Vehicle> findAllByDepartment(String department);
    List<Vehicle> findAllByType(String type);
    List<Vehicle> findAllByDepartmentAndType(String department, String type);
    Vehicle createNewVehicle(String internalCode, String name, int year, String department, String type, String condition, String minCheckInterval, String maxCheckInterval, String counter);
    Vehicle editVehicle(String internalCode, String name, int year, String department, String type, String condition, String minCheckInterval, String maxCheckInterval, String counter);
    Vehicle deleteVehicle(String internalCode);
    void insertDistanceOrHoursForVehicle(String internalCode, String information, LocalDateTime insertTime);
    void changeVehicleCondition(String internalCode);
    void checkConditionForVehicle(String internalCode, String checkType, String previousCondition, String currentCondition, String note, LocalDateTime checkTime);

}
