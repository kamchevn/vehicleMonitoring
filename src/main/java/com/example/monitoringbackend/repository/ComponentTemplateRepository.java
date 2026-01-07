package com.example.monitoringbackend.repository;

import com.example.monitoringbackend.model.ComponentTemplate;
import com.example.monitoringbackend.model.ComponentType;
import com.example.monitoringbackend.model.VehicleType;
import com.example.monitoringbackend.repository.jpa.JpaSpecificationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComponentTemplateRepository extends JpaSpecificationRepository<ComponentTemplate, Long> {
    boolean existsByNameAndVehicleType(String name,VehicleType vehicleType);
    List<ComponentTemplate> findAllByVehicleType (VehicleType vehicleType);
    List<ComponentTemplate> findAllByVehicleTypeAndComponentType (VehicleType vehicleType, ComponentType componentType);
}
