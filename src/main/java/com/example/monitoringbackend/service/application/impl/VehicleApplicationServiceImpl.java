package com.example.monitoringbackend.service.application.impl;

import com.example.monitoringbackend.model.Vehicle;
import com.example.monitoringbackend.model.dto.DisplayVehicleDto;
import com.example.monitoringbackend.service.application.VehicleApplicationService;
import com.example.monitoringbackend.service.domain.VehicleService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class VehicleApplicationServiceImpl implements VehicleApplicationService {
  private final VehicleService vehicleService;

  public VehicleApplicationServiceImpl(VehicleService vehicleService) {
    this.vehicleService = vehicleService;
  }

  @Override
  public DisplayVehicleDto findById(Long id) {
    Vehicle vehicle = vehicleService.findById(id);
    return DisplayVehicleDto.from(vehicle);
  }

  @Override
  public Page<DisplayVehicleDto> findPage(
      UserDetails user, String name, String type, Integer pageNum, Integer pageSize) {
    Page<Vehicle> page = this.vehicleService.findPage(user, name, type, pageNum, pageSize);
    return page.map(DisplayVehicleDto::from);
  }

  @Override
  public List<DisplayVehicleDto> findAll(UserDetails user) {
    List<Vehicle> vehicles = vehicleService.findAll(user);
    return vehicles.stream().map(DisplayVehicleDto::from).toList();
  }

  @Override
  public DisplayVehicleDto createNewVehicle(
      String name,
      int year,
      int totalKilometers,
      String type,
      String fuelType,
      String coolingType,
      String drivenType,
      String condition,
      String insertPeriodType,
      UserDetails user) {
    Vehicle vehicle =
        vehicleService.createNewVehicle(
            name,
            year,
            totalKilometers,
            type,
            fuelType,
            coolingType,
            drivenType,
            condition,
            insertPeriodType,
            user);
    return DisplayVehicleDto.from(vehicle);
  }

  @Override
  public DisplayVehicleDto editVehicle(
      Long id,
      String name,
      int year,
      int totalKilometers,
      String type,
      String fuelType,
      String coolingType,
      String drivenType,
      String condition,
      String insertPeriodType,
      UserDetails user) {
    Vehicle vehicle =
        vehicleService.editVehicle(
            id,
            name,
            year,
            totalKilometers,
            type,
            fuelType,
            coolingType,
            drivenType,
            condition,
            insertPeriodType,
            user);
    return DisplayVehicleDto.from(vehicle);
  }

  @Override
  public DisplayVehicleDto deleteVehicle(Long id) {
    Vehicle vehicle = vehicleService.deleteVehicle(id);
    return DisplayVehicleDto.from(vehicle);
  }
}
