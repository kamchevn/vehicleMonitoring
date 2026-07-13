package com.example.monitoringbackend.service.application.impl;

import com.example.monitoringbackend.model.Component;
import com.example.monitoringbackend.model.Vehicle;
import com.example.monitoringbackend.model.dto.DisplayComponentDto;
import com.example.monitoringbackend.model.enumerations.Condition;
import com.example.monitoringbackend.service.application.ComponentApplicationService;
import com.example.monitoringbackend.service.domain.ComponentService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class ComponentApplicationServiceImpl implements ComponentApplicationService {
  private final ComponentService componentService;

  public ComponentApplicationServiceImpl(ComponentService componentService) {
    this.componentService = componentService;
  }

  @Override
  public DisplayComponentDto findById(Long id) {
    return DisplayComponentDto.from(componentService.findById(id));
  }

  @Override
  public Page<DisplayComponentDto> findPage(
      Long vehicleId,
      String measuringUnit,
      List<String> conditions,
      Integer pageNum,
      Integer pageSize) {
    Page<Component> page =
        componentService.findPage(vehicleId, measuringUnit, conditions, pageNum, pageSize);
    return page.map(DisplayComponentDto::from);
  }

  @Override
  public List<DisplayComponentDto> findAll() {
    List<Component> components = componentService.findAll();
    return components.stream().map(DisplayComponentDto::from).toList();
  }

  @Override
  public DisplayComponentDto save(Component component) {
    return DisplayComponentDto.from(componentService.save(component));
  }

  @Override
  public List<DisplayComponentDto> saveAll(List<Component> components) {
    List<Component> componentList = componentService.saveAll(components);
    return componentList.stream().map(DisplayComponentDto::from).toList();
  }

  @Override
  public DisplayComponentDto createNewComponent(
      Vehicle vehicle, Long componentTemplateId, String condition, Integer counter) {
    return DisplayComponentDto.from(
        componentService.createNewComponent(vehicle, componentTemplateId, condition, counter));
  }

  @Override
  public DisplayComponentDto editComponent(
      Long id, Vehicle vehicle, Long componentTemplateId, String condition, Integer counter) {
    return DisplayComponentDto.from(
        componentService.editComponent(id, vehicle, componentTemplateId, condition, counter));
  }

  @Override
  public DisplayComponentDto deleteComponent(Long id) {
    return DisplayComponentDto.from(componentService.deleteComponent(id));
  }

  @Override
  public List<DisplayComponentDto> findAllByVehicleAndTemplateMeasuringUnitType(
      Vehicle vehicle, String measuringUnitType) {
    List<Component> components =
        componentService.findAllByVehicleAndTemplateMeasuringUnitType(vehicle, measuringUnitType);
    return components.stream().map(DisplayComponentDto::from).toList();
  }

  @Override
  public List<DisplayComponentDto> findAllByVehicle(Vehicle vehicle) {
    List<Component> components = componentService.findAllByVehicle(vehicle);
    return components.stream().map(DisplayComponentDto::from).toList();
  }

  @Override
  public List<DisplayComponentDto> findAllByVehicleAndCondition(
      Vehicle vehicle, Condition condition) {
    List<Component> components = componentService.findAllByVehicleAndCondition(vehicle, condition);
    return components.stream().map(DisplayComponentDto::from).toList();
  }
}
