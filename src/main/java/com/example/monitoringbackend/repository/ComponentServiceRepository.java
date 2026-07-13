package com.example.monitoringbackend.repository;

import com.example.monitoringbackend.model.ComponentService;
import com.example.monitoringbackend.model.Vehicle;
import com.example.monitoringbackend.repository.jpa.JpaSpecificationRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentServiceRepository
    extends JpaSpecificationRepository<ComponentService, Long> {
  List<ComponentService> findAllByVehicle(Vehicle vehicle);
}
