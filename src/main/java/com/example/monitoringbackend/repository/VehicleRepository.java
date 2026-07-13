package com.example.monitoringbackend.repository;

import com.example.monitoringbackend.model.Vehicle;
import com.example.monitoringbackend.repository.jpa.JpaSpecificationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaSpecificationRepository<Vehicle, Long> {
    List<Vehicle> findAllByUserUsername(String username);
}
