package com.example.monitoringbackend.service.domain.impl;

import com.example.monitoringbackend.model.enumerations.Condition;
import com.example.monitoringbackend.model.enumerations.IntervalInsertPeriod;
import com.example.monitoringbackend.model.Vehicle;
import com.example.monitoringbackend.model.dto.InsertWindowStatusDto;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class InsertWindowStatusService {

    // Weekly window: last 2 days of the week (Mon=1..Sun=7)
    private boolean isInLastTwoDaysOfWeek(LocalDateTime time) {
        LocalDate today = LocalDate.now();
        int dow = today.getDayOfWeek().getValue(); // 1=Mon .. 7=Sun

        LocalDate start = today.minusDays(dow - 6); // last 2 days = Sat+Sun
        LocalDate end = today;

        LocalDate check = time.toLocalDate();
        return !check.isBefore(start) && !check.isAfter(end);
    }

    // Monthly window: normal (last 4 days of current month) + late (first 4 days of next month)
    private boolean isInMonthlyWindow(LocalDateTime lastInsert, LocalDateTime now) {
        LocalDate today = now.toLocalDate();
        LocalDate check = lastInsert.toLocalDate();

        // Last 4 days of current month
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        LocalDate startNormal = lastDayOfMonth.minusDays(3);

        // First 4 days of next month
        LocalDate firstDayNextMonth = lastDayOfMonth.plusDays(1);
        LocalDate endLate = firstDayNextMonth.plusDays(3);

        return (!check.isBefore(startNormal) && !check.isAfter(lastDayOfMonth)) // normal
                || (!check.isBefore(firstDayNextMonth) && !check.isAfter(endLate)); // late
    }

    private boolean isInCurrentMonthWindow(LocalDateTime now) {
        LocalDate today = now.toLocalDate();

        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        LocalDate startNormal = lastDayOfMonth.minusDays(3);

        LocalDate firstDayNextMonth = lastDayOfMonth.plusDays(1);
        LocalDate endLate = firstDayNextMonth.plusDays(3);

        // Today is either in normal or late window
        return (!today.isBefore(startNormal) && !today.isAfter(lastDayOfMonth))
                || (!today.isBefore(firstDayNextMonth) && !today.isAfter(endLate));
    }

    // Checks if lastInsert happened in current window
    private boolean wasInsertedThisWindow(LocalDateTime lastInsert, IntervalInsertPeriod period, Clock clock) {
        if (lastInsert == null) return false;

        LocalDateTime now = LocalDateTime.now(clock);

        return switch (period) {
            case WEEKLY -> isInLastTwoDaysOfWeek(lastInsert);
            case MONTHLY -> isInMonthlyWindow(lastInsert, now);
        };
    }

    private boolean isInInsertWindow(Vehicle vehicle, Clock clock) {
        LocalDateTime now = LocalDateTime.now(clock);

        return switch (vehicle.getInsertPeriodType()) {
            case WEEKLY -> isInLastTwoDaysOfWeek(now);
            case MONTHLY -> isInCurrentMonthWindow(now);
        };
    }

    public InsertWindowStatusDto calculate(Vehicle vehicle, Clock clock) {
        LocalDateTime now = LocalDateTime.now(clock);

        boolean inWindow = isInInsertWindow(vehicle, clock);

        boolean insertedKm = wasInsertedThisWindow(vehicle.getLastInsertKilometers(), vehicle.getInsertPeriodType(), clock);
        boolean insertedFuel = wasInsertedThisWindow(vehicle.getLastInsertFuel(), vehicle.getInsertPeriodType(), clock);

        boolean canInsertKm = vehicle.getCondition() != Condition.UNKNOWN && inWindow && !insertedKm;
        boolean canInsertFuel = vehicle.getCondition() != Condition.UNKNOWN && inWindow && !insertedFuel;

        boolean completed = insertedKm && insertedFuel;

        boolean isInsertUpcoming = !inWindow;

        return new InsertWindowStatusDto(
                inWindow,
                canInsertKm,
                canInsertFuel,
                insertedKm,
                insertedFuel,
                completed,
                isInsertUpcoming
        );
    }
}
