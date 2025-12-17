package com.example.monitoringbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class IntervalInsert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime timeOfEntry;
    @ManyToOne
    private Vehicle vehicle;
    private String interval;

    public IntervalInsert(LocalDateTime timeOfEntry, Vehicle vehicle, String interval) {
        this.timeOfEntry = timeOfEntry;
        this.vehicle = vehicle;
        this.interval = interval;
    }
}
