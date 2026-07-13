package com.example.monitoringbackend.service.domain.impl;

import com.example.monitoringbackend.exceptions.ComponentNotFoundException;
import com.example.monitoringbackend.model.*;
import com.example.monitoringbackend.model.enumerations.Condition;
import com.example.monitoringbackend.model.enumerations.IntervalUnit;
import com.example.monitoringbackend.repository.ComponentRepository;
import com.example.monitoringbackend.service.domain.ComponentService;
import com.example.monitoringbackend.service.domain.ComponentTemplateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.monitoringbackend.service.specifications.FieldFilterSpecification.*;

@Service
public class ComponentServiceImpl implements ComponentService {
    private final ComponentRepository componentRepository;
    private final ComponentTemplateService componentTemplateService;


    public ComponentServiceImpl(ComponentRepository componentRepository, ComponentTemplateService componentTemplateService) {
        this.componentRepository = componentRepository;
        this.componentTemplateService = componentTemplateService;
    }

    @Override
    public Component findById(Long id) {
        return componentRepository.findById(id).orElseThrow(() -> new ComponentNotFoundException(id));
    }

    @Override
    public Page<Component> findPage(Long vehicleId, String measuringUnit, List<String> conditions, Integer pageNum, Integer pageSize) {
        Specification<Component> spec = Specification.allOf();

        if (vehicleId != null) {
            spec = spec.and(filterEquals(Component.class, "vehicle.id", vehicleId));
        }

        if (measuringUnit != null && !measuringUnit.isEmpty()) {
            spec = spec.and(filterEqualsV(Component.class, "template.measuringUnitType", measuringUnit));
        }

        if (conditions != null && !conditions.isEmpty()) {
            spec = spec.and(filterIn(Component.class, "condition", conditions));
        }

        return this.componentRepository.findAll(
                spec,
                PageRequest.of(pageNum, pageSize)
        );
    }

    @Override
    public List<Component> findAll() {
        return componentRepository.findAll();
    }

    @Override
    public Component save(Component component) {
        return componentRepository.save(component);
    }

    @Override
    public List<Component> saveAll(List<Component> components) {
        return componentRepository.saveAll(components);
    }

    @Override
    public Component createNewComponent(Vehicle vehicle, Long componentTemplateId, String condition, Integer counter) {
        Condition cnd = Condition.valueOf(condition);
        ComponentTemplate ct = componentTemplateService.findById(componentTemplateId);
        return componentRepository.save(new Component(vehicle,ct,cnd,counter));
    }

    @Override
    public Component editComponent(Long id, Vehicle vehicle, Long componentTemplateId, String condition, Integer counter) {
        Component component = this.findById(id);
        Condition cnd = Condition.valueOf(condition);
        ComponentTemplate ct = componentTemplateService.findById(componentTemplateId);
        component.setVehicle(vehicle);
        component.setTemplate(ct);
        component.setCondition(cnd);
        component.setCounter(counter);
        return componentRepository.save(component);
    }

    @Override
    public Component deleteComponent(Long id) {
        Component component = this.findById(id);
        componentRepository.delete(component);
        return component;
    }

    @Override
    public List<Component> findAllByVehicleAndTemplateMeasuringUnitType(Vehicle vehicle, String measuringUnitType) {
        IntervalUnit unit = IntervalUnit.valueOf(measuringUnitType);
        return componentRepository.findAllByVehicleAndTemplateMeasuringUnitType(vehicle,unit);
    }

    @Override
    public List<Component> findAllByVehicle(Vehicle vehicle) {
        return componentRepository.findAllByVehicle(vehicle);
    }

    @Override
    public List<Component> findAllByVehicleAndCondition(Vehicle vehicle, Condition condition) {
        return componentRepository.findAllByVehicleAndCondition(vehicle,condition);
    }
}
