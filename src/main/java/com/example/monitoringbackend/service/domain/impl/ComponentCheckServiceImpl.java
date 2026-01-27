package com.example.monitoringbackend.service.domain.impl;


import com.example.monitoringbackend.exceptions.CheckNotFoundException;
import com.example.monitoringbackend.model.*;
import com.example.monitoringbackend.model.enumerations.Condition;
import com.example.monitoringbackend.model.enumerations.ConditionCheckType;
import com.example.monitoringbackend.repository.ComponentCheckRepository;
import com.example.monitoringbackend.service.domain.ComponentService;
import com.example.monitoringbackend.service.domain.ComponentCheckService;
import com.example.monitoringbackend.service.domain.VehicleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.monitoringbackend.service.specifications.FieldFilterSpecification.filterEquals;
import static com.example.monitoringbackend.service.specifications.FieldFilterSpecification.filterEqualsV;

@Service
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
    public ComponentCheck findById(Long checkId) {
        return componentCheckRepository.findById(checkId).orElseThrow(() -> new CheckNotFoundException(checkId));
    }

    @Override
    public Page<ComponentCheck> findPage(UserDetails user, Long vehicleId, String checkType, Integer pageNum, Integer pageSize) {
        Specification<ComponentCheck> spec = Specification.allOf();

        boolean isAdmin = user.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            spec = spec.and(filterEquals(ComponentCheck.class, "vehicle.user.username", user.getUsername()));
        }

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
            ComponentTemplate template = component.getTemplate();
            int counter;

            Condition con = detail.getCurrentCondition();
            if (con == Condition.VERY_GOOD) {
                counter = 0;
            } else if (con == Condition.GOOD) {
                counter = template.getWarningInterval();
            } else if (con == Condition.POOR) {
                counter = template.getMinCheckInterval();
            } else {
                counter = template.getMinCheckInterval();
                counter *= 1.3;
            }

            component.setCondition(con);
            component.setWarningFlag(con == Condition.GOOD);
            component.setNeedsCheck(con == Condition.POOR || con == Condition.OOS);
            component.setCounter(counter);
            component.setLastChecked(LocalDateTime.now());

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
