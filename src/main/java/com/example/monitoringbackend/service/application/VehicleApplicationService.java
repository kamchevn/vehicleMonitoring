package com.example.monitoringbackend.service.application;

import com.example.monitoringbackend.model.dto.DisplayVehicleDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

public interface VehicleApplicationService {
  DisplayVehicleDto findById(Long id);

  Page<DisplayVehicleDto> findPage(
      UserDetails user, String name, String type, Integer pageNum, Integer pageSize);

  List<DisplayVehicleDto> findAll(UserDetails user);

  DisplayVehicleDto createNewVehicle(
      String name,
      int year,
      int totalKilometers,
      String type,
      String fuelType,
      String coolingType,
      String drivenType,
      String condition,
      String insertPeriodType,
      UserDetails user);

  DisplayVehicleDto editVehicle(
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
      UserDetails user);

  DisplayVehicleDto deleteVehicle(Long id);
}
