package com.example.monitoringbackend.repository;

import com.example.monitoringbackend.model.Department;
import com.example.monitoringbackend.model.Vehicle;
import com.example.monitoringbackend.repository.jpa.JpaSpecificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaSpecificationRepository<Vehicle, String> {
    List<Vehicle> findAllByDepartment(Department department);
    List<Vehicle> findAllByTypeIgnoreCase(String type);
    List<Vehicle> findAllByDepartmentAndType(Department department, String type);
}
