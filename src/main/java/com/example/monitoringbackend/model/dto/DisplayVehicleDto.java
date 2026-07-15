package com.example.monitoringbackend.model.dto;

import com.example.monitoringbackend.model.Vehicle;
import com.example.monitoringbackend.model.enumerations.*;
import java.time.LocalDateTime;

public record DisplayVehicleDto(
    Long id,
    String name,
    Integer year,
    Integer totalKilometers,
    VehicleType type,
    VehicleFuelType fuelType,
    CoolingType coolingType,
    DrivenType drivenType,
    Condition condition,
    boolean needsCheck,
    boolean hasWarnings,
    IntervalInsertPeriod insertPeriodType,
    LocalDateTime lastInsertKilometers,
    LocalDateTime lastInsertFuel,
    InsertWindowStatusDto insertWindowStatus,
    String username) {
  public static DisplayVehicleDto from(Vehicle vehicle) {

    return new DisplayVehicleDto(
        vehicle.getId(),
        vehicle.getName(),
        vehicle.getYear(),
        vehicle.getTotalKilometers(),
        vehicle.getType(),
        vehicle.getFuelType(),
        vehicle.getCoolingType(),
        vehicle.getDrivenType(),
        vehicle.getCondition(),
        vehicle.isNeedsCheck(),
        vehicle.isHasWarnings(),
        vehicle.getInsertPeriodType(),
        vehicle.getLastInsertKilometers(),
        vehicle.getLastInsertFuel(),
        vehicle.getInsertWindowStatus(),
        vehicle.getUser().getUsername());
  }
}
