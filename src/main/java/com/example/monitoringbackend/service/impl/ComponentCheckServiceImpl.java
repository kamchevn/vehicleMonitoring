package com.example.monitoringbackend.service.impl;


import com.example.monitoringbackend.model.*;
import com.example.monitoringbackend.repository.ComponentCheckRepository;
import com.example.monitoringbackend.service.ComponentService;
import com.example.monitoringbackend.service.ComponentCheckService;
import com.example.monitoringbackend.service.VehicleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.monitoringbackend.service.specifications.FieldFilterSpecification.filterEquals;
import static com.example.monitoringbackend.service.specifications.FieldFilterSpecification.filterEqualsV;

@org.springframework.stereotype.Service
public class ComponentCheckServiceImpl implements ComponentCheckService {
    private final ComponentCheckRepository componentCheckRepository;
    private final VehicleService vehicleService;
    private final ComponentService componentService;

    public ComponentCheckServiceImpl(ComponentCheckRepository componentCheckRepository, VehicleService vehicleService, ComponentService componentService) {
        this.componentCheckRepository = componentCheckRepository;
        this.vehicleService = vehicleService;
        this.componentService = componentService;
    }

    @Override
    public Page<ComponentCheck> findPage(Long vehicleId, String checkType, Integer pageNum, Integer pageSize) {
        Specification<ComponentCheck> spec = Specification.allOf();

        if (vehicleId != null) {
            spec = spec.and(filterEquals(ComponentCheck.class, "vehicle.id", vehicleId));
        }

        if (checkType != null && !checkType.isEmpty()) {
            ConditionCheckType typeEnum = ConditionCheckType.valueOf(checkType);
            spec = spec.and(filterEqualsV(ComponentCheck.class, "checkType", typeEnum));
        }

        return this.componentCheckRepository.findAll(
                spec,
                PageRequest.of(pageNum, pageSize)
        );
    }

    @Override
    public void checkConditionForVehicle(Long vehicleId, String checkType, String note, LocalDateTime checkTime, List<ComponentCheckDetail> componentDetails) {
        Vehicle vehicle = vehicleService.findById(vehicleId);
        Condition previousVehicleCondition = vehicle.getCondition();

        ComponentCheck check = new ComponentCheck();
        check.setVehicle(vehicle);
        check.setCheckType(ConditionCheckType.valueOf(checkType));
        check.setPreviousCondition(previousVehicleCondition);
        check.setTimeOfEntry(checkTime);
        check.setNote((note == null || note.isBlank()) ? null : note);

        for (ComponentCheckDetail detail : componentDetails) {
            Component component = componentService.findById(detail.getComponent().getId());
            if (detail.getPreviousCondition() == null) {
                detail.setPreviousCondition(component.getCondition());
            }
            component.setCondition(detail.getCurrentCondition());
            component.setCounter(0);
            component.setNeedsCheck(false);

            componentService.save(component);

            detail.setComponentCheck(check);
            detail.setComponent(component);

            check.addComponentDetail(detail);
        }

        vehicleService.changeVehicleCondition(vehicleId);
        Vehicle updatedVehicle = vehicleService.findById(vehicleId);
        check.setCurrentCondition(updatedVehicle.getCondition());

        componentCheckRepository.save(check);
    }
}
