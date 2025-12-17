package com.example.monitoringbackend.repository;

import com.example.monitoringbackend.model.ConditionCheck;
import com.example.monitoringbackend.repository.jpa.JpaSpecificationRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConditionCheckRepository extends JpaSpecificationRepository<ConditionCheck,Long> {
}
