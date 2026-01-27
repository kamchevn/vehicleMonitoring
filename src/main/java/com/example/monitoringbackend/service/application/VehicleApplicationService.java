package com.example.monitoringbackend.service.application;

import com.example.monitoringbackend.model.Vehicle;
import com.example.monitoringbackend.model.dto.DisplayVehicleDto;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface VehicleApplicationService {
    DisplayVehicleDto findById(Long id);
    Page<DisplayVehicleDto> findPage(UserDetails user, String name, String type, Integer pageNum, Integer pageSize);
    List<DisplayVehicleDto> findAll();
    DisplayVehicleDto createNewVehicle(String name, int year, int totalKilometers, String type, String fuelType, String coolingType, String drivenType, String condition, String insertPeriodType, UserDetails user);
    DisplayVehicleDto editVehicle(Long id, String name, int year, int totalKilometers, String type, String fuelType, String coolingType, String drivenType, String condition, String insertPeriodType, UserDetails user);
    DisplayVehicleDto deleteVehicle(Long id);
}
