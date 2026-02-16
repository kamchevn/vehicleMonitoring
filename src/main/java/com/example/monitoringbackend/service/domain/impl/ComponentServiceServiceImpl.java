package com.example.monitoringbackend.service.domain.impl;


import com.example.monitoringbackend.exceptions.ServiceNotFoundException;
import com.example.monitoringbackend.model.*;
import com.example.monitoringbackend.model.enumerations.Condition;
import com.example.monitoringbackend.model.enumerations.ServiceType;
import com.example.monitoringbackend.repository.ComponentServiceRepository;
import com.example.monitoringbackend.service.domain.ComponentServiceService;
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
public class ComponentServiceServiceImpl implements ComponentServiceService {
    private final ComponentServiceRepository componentServiceRepository;
    private final VehicleService vehicleService;
    private final com.example.monitoringbackend.service.domain.ComponentService componentService;

    public ComponentServiceServiceImpl(ComponentServiceRepository componentServiceRepository, VehicleService vehicleService, com.example.monitoringbackend.service.domain.ComponentService componentService) {
        this.componentServiceRepository = componentServiceRepository;
        this.vehicleService = vehicleService;
        this.componentService = componentService;
    }

    @Override
    public ComponentService findById(Long checkId) {
        return componentServiceRepository.findById(checkId).orElseThrow(() -> new ServiceNotFoundException(checkId));
    }

    @Override
    public Page<ComponentService> findPage(UserDetails user, Long vehicleId, String serviceType, Integer pageNum, Integer pageSize) {
        Specification<ComponentService> spec = Specification.allOf();

        boolean isAdmin = user.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            spec = spec.and(filterEquals(ComponentService.class, "vehicle.user.username", user.getUsername()));
        }

        if (vehicleId != null) {
            spec = spec.and(filterEquals(ComponentService.class, "vehicle.id", vehicleId));
        }

        if (serviceType != null && !serviceType.isEmpty()) {
            ServiceType typeEnum = ServiceType.valueOf(serviceType);
            spec = spec.and(filterEqualsV(ComponentService.class, "serviceType", typeEnum));
        }

        return this.componentServiceRepository.findAll(
                spec,
                PageRequest.of(pageNum, pageSize)
        );
    }

    @Override
    public void checkConditionForVehicle(Long vehicleId, String serviceType, String note, LocalDateTime checkTime, List<ComponentServiceDetail> componentDetails) {
        Vehicle vehicle = vehicleService.findById(vehicleId);
        Condition previousVehicleCondition = vehicle.getCondition();

        ComponentService check = new ComponentService();
        check.setVehicle(vehicle);
        check.setServiceType(ServiceType.valueOf(serviceType));
        check.setPreviousCondition(previousVehicleCondition);
        check.setTimeOfEntry(checkTime);
        check.setNote((note == null || note.isBlank()) ? null : note);

        for (ComponentServiceDetail detail : componentDetails) {
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
        componentServiceRepository.save(check);
    }
}
