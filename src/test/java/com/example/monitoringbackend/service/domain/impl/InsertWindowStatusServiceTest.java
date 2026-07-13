package com.example.monitoringbackend.service.domain.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.monitoringbackend.model.IntervalInsert;
import com.example.monitoringbackend.model.Vehicle;
import com.example.monitoringbackend.model.dto.InsertWindowStatusDto;
import com.example.monitoringbackend.model.enumerations.Condition;
import com.example.monitoringbackend.model.enumerations.IntervalInsertPeriod;
import com.example.monitoringbackend.model.enumerations.IntervalUnit;
import com.example.monitoringbackend.repository.IntervalInsertRepository;
import com.example.monitoringbackend.repository.VehicleRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InsertWindowStatusServiceTest {

  private static final ZoneId ZONE = ZoneId.of("UTC");

  @Mock private IntervalInsertRepository intervalInsertRepository;

  @Mock private VehicleRepository vehicleRepository;

  @InjectMocks private InsertWindowStatusService service;

  private Vehicle vehicle;

  @BeforeEach
  void setUp() {
    vehicle = new Vehicle();
    vehicle.setCondition(Condition.GOOD);
    vehicle.setInsertPeriodType(IntervalInsertPeriod.WEEKLY);
    when(intervalInsertRepository.findTop6ByVehicleOrderByTimeOfEntryDesc(vehicle))
        .thenReturn(List.of());
  }

  private Clock fixedClock(String isoInstant) {
    return Clock.fixed(Instant.parse(isoInstant), ZONE);
  }

  @Test
  void calculate_weekly_allowsInsertsOnSaturdayWithinWindow() {
    // Saturday 2026-07-11
    Clock clock = fixedClock("2026-07-11T12:00:00Z");

    InsertWindowStatusDto dto = service.calculate(vehicle, clock);

    assertTrue(dto.inInsertWindow());
    assertTrue(dto.canInsertKilometers());
    assertTrue(dto.canInsertFuel());
    assertFalse(dto.hasInsertedKilometersThisWindow());
    assertFalse(dto.hasInsertedFuelThisWindow());
    assertFalse(dto.isPenaltyActive());
  }

  @Test
  void calculate_weekly_blocksKmWhenAlreadyInsertedThisWindow() {
    Clock clock = fixedClock("2026-07-11T12:00:00Z");
    vehicle.setLastInsertKilometers(LocalDateTime.of(2026, 7, 11, 8, 0));

    InsertWindowStatusDto dto = service.calculate(vehicle, clock);

    assertTrue(dto.inInsertWindow());
    assertFalse(dto.canInsertKilometers());
    assertTrue(dto.canInsertFuel());
    assertTrue(dto.hasInsertedKilometersThisWindow());
  }

  @Test
  void calculate_weekly_lateWindowIsStillInWindow() {
    // Monday 2026-07-13
    Clock clock = fixedClock("2026-07-13T12:00:00Z");

    InsertWindowStatusDto dto = service.calculate(vehicle, clock);

    assertTrue(dto.inInsertWindow());
    assertTrue(dto.canInsertKilometers());
    assertTrue(dto.canInsertFuel());
  }

  @Test
  void calculate_blocksInsertsWhenConditionUnknown() {
    Clock clock = fixedClock("2026-07-11T12:00:00Z");
    vehicle.setCondition(Condition.UNKNOWN);

    InsertWindowStatusDto dto = service.calculate(vehicle, clock);

    assertTrue(dto.inInsertWindow());
    assertFalse(dto.canInsertKilometers());
    assertFalse(dto.canInsertFuel());
  }

  @Test
  void calculate_outsideWindow_marksInsertUpcoming() {
    // Wednesday 2026-07-15
    Clock clock = fixedClock("2026-07-15T12:00:00Z");

    InsertWindowStatusDto dto = service.calculate(vehicle, clock);

    assertFalse(dto.inInsertWindow());
    assertFalse(dto.canInsertKilometers());
    assertTrue(dto.isInsertUpcoming());
  }

  @Test
  void calculate_activatesPenalty_whenLastSixAllLateWithBalancedUnits() {
    Clock clock = fixedClock("2026-07-11T12:00:00Z");
    List<IntervalInsert> lateInserts =
        List.of(
            lateInsert(IntervalUnit.KILOMETERS),
            lateInsert(IntervalUnit.KILOMETERS),
            lateInsert(IntervalUnit.KILOMETERS),
            lateInsert(IntervalUnit.BURNT_FUEL),
            lateInsert(IntervalUnit.BURNT_FUEL),
            lateInsert(IntervalUnit.BURNT_FUEL));
    when(intervalInsertRepository.findTop6ByVehicleOrderByTimeOfEntryDesc(vehicle))
        .thenReturn(lateInserts);

    InsertWindowStatusDto dto = service.calculate(vehicle, clock);

    assertTrue(dto.isPenaltyActive());
    assertTrue(vehicle.isPenaltyActive());
    assertEquals(3, vehicle.getPenaltyKmRemaining());
    assertEquals(3, vehicle.getPenaltyFuelRemaining());
    verify(vehicleRepository).save(vehicle);
  }

  @Test
  void calculate_penaltyBlocksInsertsDuringLateWindow() {
    // Monday = weekly late window
    Clock clock = fixedClock("2026-07-13T12:00:00Z");
    vehicle.setPenaltyActive(true);

    InsertWindowStatusDto dto = service.calculate(vehicle, clock);

    assertTrue(dto.inInsertWindow());
    assertFalse(dto.canInsertKilometers());
    assertFalse(dto.canInsertFuel());
    assertTrue(dto.isPenaltyActive());
  }

  @Test
  void calculate_monthly_allowsInsertsNearEndOfMonth() {
    vehicle.setInsertPeriodType(IntervalInsertPeriod.MONTHLY);
    // 2026-07-28 is within last 4 days of July
    Clock clock = fixedClock("2026-07-28T12:00:00Z");

    InsertWindowStatusDto dto = service.calculate(vehicle, clock);

    assertTrue(dto.inInsertWindow());
    assertTrue(dto.canInsertKilometers());
    assertTrue(dto.canInsertFuel());
  }

  private IntervalInsert lateInsert(IntervalUnit unit) {
    IntervalInsert insert = new IntervalInsert();
    insert.setUnit(unit);
    insert.setLate(true);
    return insert;
  }
}
