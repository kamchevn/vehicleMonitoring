package com.example.monitoringbackend.repository;

import com.example.monitoringbackend.model.Component;
import com.example.monitoringbackend.model.enumerations.Condition;
import com.example.monitoringbackend.model.enumerations.IntervalUnit;
import com.example.monitoringbackend.model.Vehicle;
import com.example.monitoringbackend.repository.jpa.JpaSpecificationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComponentRepository extends JpaSpecificationRepository<Component,Long> {
    List<Component> findAllByVehicle(Vehicle vehicle);
    List<Component> findAllByVehicleAndTemplateMeasuringUnitType(Vehicle vehicle, IntervalUnit measuringUnitType);
    List<Component> findAllByVehicleAndCondition(Vehicle vehicle, Condition condition);
}
