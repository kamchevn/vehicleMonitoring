package com.example.monitoringbackend.service.application;

import com.example.monitoringbackend.model.Component;
import com.example.monitoringbackend.model.Vehicle;
import com.example.monitoringbackend.model.dto.DisplayComponentDto;
import com.example.monitoringbackend.model.enumerations.Condition;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ComponentApplicationService {
    DisplayComponentDto findById(Long id);
    Page<DisplayComponentDto> findPage(Long vehicleId, String measuringUnit, List<String> conditions, Integer pageNum, Integer pageSize);
    List<DisplayComponentDto> findAll();
    DisplayComponentDto save(Component component);
    List<DisplayComponentDto> saveAll(List<Component> components);
    DisplayComponentDto createNewComponent(Vehicle vehicle, Long componentTemplateId, String condition, Integer counter);
    DisplayComponentDto editComponent(Long id, Vehicle vehicle, Long componentTemplateId, String condition, Integer counter);
    DisplayComponentDto deleteComponent(Long id);
    List<DisplayComponentDto> findAllByVehicleAndTemplateMeasuringUnitType(Vehicle vehicle, String measuringUnitType);
    List<DisplayComponentDto> findAllByVehicle(Vehicle vehicle);
    List<DisplayComponentDto> findAllByVehicleAndCondition(Vehicle vehicle, Condition condition);
}
