package com.example.monitoringbackend.service.domain;

import com.example.monitoringbackend.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VehicleService {
    Vehicle findById(Long id);
    Page<Vehicle> findPage(UserDetails user, String name, String type, Integer pageNum, Integer pageSize);
    List<Vehicle> findAll(UserDetails user);
    Vehicle createNewVehicle(String name, int year, int totalKilometers, String type, String fuelType, String coolingType, String drivenType, String condition, String insertPeriodType, UserDetails user);
    Vehicle editVehicle(Long id, String name, int year, int totalKilometers, String type, String fuelType, String coolingType, String drivenType, String condition, String insertPeriodType, UserDetails user);
    Vehicle deleteVehicle(Long id);
    void insertDistanceOrFuelForVehicle(Long vehicleId, String unitType, Integer amount, LocalDateTime insertTime, boolean late);
    void changeVehicleCondition(Long id);

}
