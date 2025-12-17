package com.example.monitoringbackend.repository;

import com.example.monitoringbackend.model.IntervalInsert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntervalInsertRepository extends JpaRepository<IntervalInsert,Long> {
}
