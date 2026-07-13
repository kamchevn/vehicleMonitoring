package com.example.monitoringbackend.repository;

import com.example.monitoringbackend.model.ComponentTemplate;
import com.example.monitoringbackend.model.enumerations.ComponentType;
import com.example.monitoringbackend.model.enumerations.VehicleType;
import com.example.monitoringbackend.repository.jpa.JpaSpecificationRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentTemplateRepository
    extends JpaSpecificationRepository<ComponentTemplate, Long> {
  boolean existsByNameAndVehicleType(String name, VehicleType vehicleType);

  List<ComponentTemplate> findAllByVehicleType(VehicleType vehicleType);

  List<ComponentTemplate> findAllByVehicleTypeAndComponentType(
      VehicleType vehicleType, ComponentType componentType);
}
