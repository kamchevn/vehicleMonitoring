package com.example.monitoringbackend.repository;

import com.example.monitoringbackend.model.ComponentCheck;
import com.example.monitoringbackend.repository.jpa.JpaSpecificationRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentCheckRepository extends JpaSpecificationRepository<ComponentCheck,Long> {
}
