package com.example.monitoringbackend.service.domain.impl;

import com.example.monitoringbackend.model.IntervalInsert;
import com.example.monitoringbackend.model.Vehicle;
import com.example.monitoringbackend.model.dto.InsertWindowStatusDto;
import com.example.monitoringbackend.model.enumerations.Condition;
import com.example.monitoringbackend.model.enumerations.IntervalInsertPeriod;
import com.example.monitoringbackend.model.enumerations.IntervalUnit;
import com.example.monitoringbackend.repository.IntervalInsertRepository;
import com.example.monitoringbackend.repository.VehicleRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class InsertWindowStatusService {

  private final IntervalInsertRepository intervalInsertRepository;
  private final VehicleRepository vehicleRepository;

  public InsertWindowStatusService(
      IntervalInsertRepository intervalInsertRepository, VehicleRepository vehicleRepository) {
    this.intervalInsertRepository = intervalInsertRepository;
    this.vehicleRepository = vehicleRepository;
  }

  private LocalDate getWeeklyWindowStart(LocalDate date) {
    int dow = date.getDayOfWeek().getValue(); // 1=Mon ... 7=Sun
    int daysFromSaturday = (dow + 1) % 7;
    return date.minusDays(daysFromSaturday);
  }

  private LocalDate getMonthlyWindowStart(LocalDate date) {
    LocalDate lastDay = date.withDayOfMonth(date.lengthOfMonth());

    // Normal window: last 4 days of current month
    if (!date.isBefore(lastDay.minusDays(3))) {
      return lastDay.minusDays(3);
    }

    // Late window: first 4 days of month → window started last month
    if (date.getDayOfMonth() <= 4) {
      LocalDate prevMonthLastDay =
          date.minusMonths(1).withDayOfMonth(date.minusMonths(1).lengthOfMonth());
      return prevMonthLastDay.minusDays(3);
    }

    // Outside any monthly window
    return null;
  }

  private boolean isWeeklyNormalWindow(LocalDate date) {
    int dow = date.getDayOfWeek().getValue();
    return dow == 6 || dow == 7; // Saturday, Sunday
  }

  private boolean isWeeklyLateWindow(LocalDate date) {
    int dow = date.getDayOfWeek().getValue();
    return dow == 1 || dow == 2; // Monday, Tuesday
  }

  private boolean isMonthlyNormalWindow(LocalDate date) {
    LocalDate lastDay = date.withDayOfMonth(date.lengthOfMonth());
    LocalDate startNormal = lastDay.minusDays(3);

    LocalDate firstNextMonth = lastDay.plusDays(1);
    LocalDate endLate = firstNextMonth.plusDays(3);

    return (!date.isBefore(startNormal) && !date.isAfter(lastDay))
        || (!date.isBefore(firstNextMonth) && !date.isAfter(endLate));
  }

  private boolean isMonthlyLateWindow(LocalDate date) {
    return date.getDayOfMonth() <= 4;
  }

  private boolean wasInsertedInCurrentWeeklyWindow(LocalDateTime lastInsert, Clock clock) {
    if (lastInsert == null) return false;

    LocalDate today = LocalDate.now(clock);

    LocalDate currentWindowStart = getWeeklyWindowStart(today);
    LocalDate insertWindowStart = getWeeklyWindowStart(lastInsert.toLocalDate());

    return currentWindowStart.equals(insertWindowStart);
  }

  private boolean wasInsertedInCurrentMonthlyWindow(LocalDateTime lastInsert, Clock clock) {
    if (lastInsert == null) return false;

    LocalDate today = LocalDate.now(clock);

    LocalDate currentWindowStart = getMonthlyWindowStart(today);
    LocalDate insertWindowStart = getMonthlyWindowStart(lastInsert.toLocalDate());

    if (currentWindowStart == null || insertWindowStart == null) {
      return false;
    }

    return currentWindowStart.equals(insertWindowStart);
  }

  private boolean evaluateAndUpdatePenaltyStatus(Vehicle vehicle) {

    List<IntervalInsert> lastSix =
        intervalInsertRepository.findTop6ByVehicleOrderByTimeOfEntryDesc(vehicle);

    if (lastSix.size() < 6) {
      return vehicle.isPenaltyActive();
    }

    if (vehicle.isPenaltyActive()) {
      return true;
    }

    if (lastSix.stream().allMatch(IntervalInsert::isLate)) {

      long kmLate = lastSix.stream().filter(i -> i.getUnit() == IntervalUnit.KILOMETERS).count();

      long fuelLate = lastSix.stream().filter(i -> i.getUnit() == IntervalUnit.BURNT_FUEL).count();

      if (kmLate == 3 && fuelLate == 3) {
        vehicle.setPenaltyActive(true);
        vehicle.setPenaltyKmRemaining(3);
        vehicle.setPenaltyFuelRemaining(3);
        vehicleRepository.save(vehicle);
        return true;
      }
    }

    return false;
  }

  private boolean isInInsertWindow(Vehicle vehicle, Clock clock) {
    LocalDate today = LocalDate.now(clock);

    return switch (vehicle.getInsertPeriodType()) {
      case WEEKLY -> isWeeklyNormalWindow(today) || isWeeklyLateWindow(today);
      case MONTHLY -> isMonthlyNormalWindow(today) || isMonthlyLateWindow(today);
    };
  }

  public InsertWindowStatusDto calculate(Vehicle vehicle, Clock clock) {

    boolean penaltyActive = evaluateAndUpdatePenaltyStatus(vehicle);

    boolean inWindow = isInInsertWindow(vehicle, clock);

    boolean insertedKmThisWindow;
    boolean insertedFuelThisWindow;

    if (vehicle.getInsertPeriodType() == IntervalInsertPeriod.WEEKLY) {
      insertedKmThisWindow =
          wasInsertedInCurrentWeeklyWindow(vehicle.getLastInsertKilometers(), clock);
      insertedFuelThisWindow = wasInsertedInCurrentWeeklyWindow(vehicle.getLastInsertFuel(), clock);
    } else {
      insertedKmThisWindow =
          wasInsertedInCurrentMonthlyWindow(vehicle.getLastInsertKilometers(), clock);
      insertedFuelThisWindow =
          wasInsertedInCurrentMonthlyWindow(vehicle.getLastInsertFuel(), clock);
    }

    boolean canInsertKm =
        vehicle.getCondition() != Condition.UNKNOWN && inWindow && !insertedKmThisWindow;

    boolean canInsertFuel =
        vehicle.getCondition() != Condition.UNKNOWN && inWindow && !insertedFuelThisWindow;

    LocalDate today = LocalDate.now(clock);

    boolean isNormalWindowToday =
        vehicle.getInsertPeriodType() == IntervalInsertPeriod.WEEKLY
            ? isWeeklyNormalWindow(today)
            : isMonthlyNormalWindow(today);

    boolean isLateWindowToday =
        vehicle.getInsertPeriodType() == IntervalInsertPeriod.WEEKLY
            ? isWeeklyLateWindow(today)
            : isMonthlyLateWindow(today);

    if (penaltyActive && isLateWindowToday) {
      canInsertKm = false;
      canInsertFuel = false;
    }

    boolean completed = insertedKmThisWindow && insertedFuelThisWindow && isNormalWindowToday;

    boolean isInsertUpcoming;

    if (vehicle.getInsertPeriodType() == IntervalInsertPeriod.WEEKLY) {
      // Completed in the normal window (Sat/Sun)
      if (insertedKmThisWindow && insertedFuelThisWindow) {
        // Today is late window (Mon/Tue)
        isInsertUpcoming = isWeeklyLateWindow(today);
      } else {
        isInsertUpcoming = false;
      }
    } else {
      if (insertedKmThisWindow && insertedFuelThisWindow) {
        isInsertUpcoming = isMonthlyLateWindow(today);
      } else {
        isInsertUpcoming = false;
      }
    }

    if (!inWindow
        && (vehicle.getLastInsertKilometers() != null || vehicle.getLastInsertFuel() != null)) {
      isInsertUpcoming = true;
    }

    if (vehicle.getLastInsertFuel() == null
        && vehicle.getLastInsertKilometers() == null
        && !inWindow) {
      isInsertUpcoming = true;
    }

    return new InsertWindowStatusDto(
        inWindow,
        canInsertKm,
        canInsertFuel,
        insertedKmThisWindow,
        insertedFuelThisWindow,
        completed,
        isInsertUpcoming,
        penaltyActive);
  }
}
