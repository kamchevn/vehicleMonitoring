package com.example.monitoringbackend.model.dto;

public record InsertWindowStatusDto(
        boolean inInsertWindow,
        boolean canInsertKilometers,
        boolean canInsertFuel,
        boolean hasInsertedKilometersThisWindow,
        boolean hasInsertedFuelThisWindow,
        boolean hasCompletedAllInserts,
        boolean isInsertUpcoming,
        boolean isPenaltyActive
) {}
