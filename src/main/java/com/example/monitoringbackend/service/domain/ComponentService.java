package com.example.monitoringbackend.service.domain;

import com.example.monitoringbackend.model.*;
import com.example.monitoringbackend.model.enumerations.Condition;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ComponentService {
  Component findById(Long id);

  Page<Component> findPage(
      Long vehicleId,
      String measuringUnit,
      List<String> conditions,
      Integer pageNum,
      Integer pageSize);

  List<Component> findAll();

  Component save(Component component);

  List<Component> saveAll(List<Component> components);

  Component createNewComponent(
      Vehicle vehicle, Long componentTemplateId, String condition, Integer counter);

  Component editComponent(
      Long id, Vehicle vehicle, Long componentTemplateId, String condition, Integer counter);

  Component deleteComponent(Long id);

  List<Component> findAllByVehicleAndTemplateMeasuringUnitType(
      Vehicle vehicle, String measuringUnitType);

  List<Component> findAllByVehicle(Vehicle vehicle);

  List<Component> findAllByVehicleAndCondition(Vehicle vehicle, Condition condition);
}
