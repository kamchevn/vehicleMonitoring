package com.example.monitoringbackend.repository;

import com.example.monitoringbackend.model.ComponentTemplate;
import com.example.monitoringbackend.model.VehicleType;
import com.example.monitoringbackend.repository.jpa.JpaSpecificationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComponentTemplateRepository extends JpaSpecificationRepository<ComponentTemplate, Long> {
    List<ComponentTemplate> findAllByVehicleType (VehicleType vehicleType);
}
