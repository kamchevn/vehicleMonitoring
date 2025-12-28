package com.example.monitoringbackend.repository;

import com.example.monitoringbackend.model.Vehicle;
import com.example.monitoringbackend.repository.jpa.JpaSpecificationRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaSpecificationRepository<Vehicle, Long> {
}
