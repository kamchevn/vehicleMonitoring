package com.example.monitoringbackend.repository;

import com.example.monitoringbackend.model.IntervalInsert;
import com.example.monitoringbackend.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IntervalInsertRepository extends JpaRepository<IntervalInsert,Long> {
    List<IntervalInsert> findAllByVehicle(Vehicle vehicle);
    List<IntervalInsert> findTop6ByVehicleOrderByTimeOfEntryDesc(Vehicle vehicle);
}
