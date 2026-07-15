package com.example.monitoringbackend.repository;

import com.example.monitoringbackend.model.Component;
import com.example.monitoringbackend.model.Vehicle;
import com.example.monitoringbackend.model.enumerations.Condition;
import com.example.monitoringbackend.model.enumerations.IntervalUnit;
import com.example.monitoringbackend.repository.jpa.JpaSpecificationRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentRepository extends JpaSpecificationRepository<Component, Long> {
  List<Component> findAllByVehicle(Vehicle vehicle);

  List<Component> findAllByVehicleAndTemplateMeasuringUnitType(
      Vehicle vehicle, IntervalUnit measuringUnitType);

  List<Component> findAllByVehicleAndCondition(Vehicle vehicle, Condition condition);
}
