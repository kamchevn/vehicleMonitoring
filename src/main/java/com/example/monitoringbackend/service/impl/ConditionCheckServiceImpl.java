package com.example.monitoringbackend.service.impl;


import com.example.monitoringbackend.model.ConditionCheck;
import com.example.monitoringbackend.model.ConditionCheckType;
import com.example.monitoringbackend.repository.ConditionCheckRepository;
import com.example.monitoringbackend.service.ConditionCheckService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import static com.example.monitoringbackend.service.specifications.FieldFilterSpecification.filterEquals;
import static com.example.monitoringbackend.service.specifications.FieldFilterSpecification.filterEqualsV;

@Service
public class ConditionCheckServiceImpl implements ConditionCheckService {
    private final ConditionCheckRepository conditionCheckRepository;

    public ConditionCheckServiceImpl(ConditionCheckRepository conditionCheckRepository) {
        this.conditionCheckRepository = conditionCheckRepository;
    }

    @Override
    public Page<ConditionCheck> findPage(String vehicleInternalCode, String checkType, Integer pageNum, Integer pageSize) {
        Specification<ConditionCheck> spec = Specification.allOf();

        if (vehicleInternalCode != null && !vehicleInternalCode.isEmpty()) {
            spec = spec.and(filterEquals(ConditionCheck.class, "vehicle.internalCode", vehicleInternalCode));
        }

        if (checkType != null && !checkType.isEmpty()) {
            ConditionCheckType typeEnum = ConditionCheckType.valueOf(checkType);
            spec = spec.and(filterEqualsV(ConditionCheck.class, "checkType", typeEnum));
        }

        return this.conditionCheckRepository.findAll(
                spec,
                PageRequest.of(pageNum, pageSize)
        );
    }
}
