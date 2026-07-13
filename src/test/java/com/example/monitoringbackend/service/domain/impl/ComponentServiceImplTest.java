package com.example.monitoringbackend.service.domain.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.monitoringbackend.exceptions.ComponentNotFoundException;
import com.example.monitoringbackend.model.Component;
import com.example.monitoringbackend.model.ComponentTemplate;
import com.example.monitoringbackend.model.Vehicle;
import com.example.monitoringbackend.model.enumerations.Condition;
import com.example.monitoringbackend.model.enumerations.IntervalUnit;
import com.example.monitoringbackend.repository.ComponentRepository;
import com.example.monitoringbackend.service.domain.ComponentTemplateService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ComponentServiceImplTest {

  @Mock private ComponentRepository componentRepository;

  @Mock private ComponentTemplateService componentTemplateService;

  @InjectMocks private ComponentServiceImpl componentService;

  private Vehicle vehicle;
  private ComponentTemplate template;
  private Component component;

  @BeforeEach
  void setUp() {
    vehicle = new Vehicle();
    ReflectionTestUtils.setField(vehicle, "id", 1L);

    template = new ComponentTemplate();
    ReflectionTestUtils.setField(template, "id", 10L);

    component = new Component(vehicle, template, Condition.GOOD, 50);
    ReflectionTestUtils.setField(component, "id", 100L);
  }

  @Test
  void findById_returnsComponent_whenPresent() {
    when(componentRepository.findById(100L)).thenReturn(Optional.of(component));

    Component result = componentService.findById(100L);

    assertEquals(100L, result.getId());
    assertEquals(Condition.GOOD, result.getCondition());
  }

  @Test
  void findById_throws_whenMissing() {
    when(componentRepository.findById(999L)).thenReturn(Optional.empty());

    assertThrows(ComponentNotFoundException.class, () -> componentService.findById(999L));
  }

  @Test
  void findAll_returnsAllComponents() {
    when(componentRepository.findAll()).thenReturn(List.of(component));

    List<Component> result = componentService.findAll();

    assertEquals(1, result.size());
    verify(componentRepository).findAll();
  }

  @Test
  void save_delegatesToRepository() {
    when(componentRepository.save(component)).thenReturn(component);

    Component result = componentService.save(component);

    assertSame(component, result);
    verify(componentRepository).save(component);
  }

  @Test
  void createNewComponent_savesWithResolvedTemplateAndCondition() {
    when(componentTemplateService.findById(10L)).thenReturn(template);
    when(componentRepository.save(any(Component.class))).thenAnswer(inv -> inv.getArgument(0));

    Component result = componentService.createNewComponent(vehicle, 10L, "VERY_GOOD", 0);

    ArgumentCaptor<Component> captor = ArgumentCaptor.forClass(Component.class);
    verify(componentRepository).save(captor.capture());
    Component saved = captor.getValue();

    assertEquals(vehicle, saved.getVehicle());
    assertEquals(template, saved.getTemplate());
    assertEquals(Condition.VERY_GOOD, saved.getCondition());
    assertEquals(0, saved.getCounter());
    assertSame(saved, result);
  }

  @Test
  void editComponent_updatesFieldsAndSaves() {
    ComponentTemplate newTemplate = new ComponentTemplate();
    ReflectionTestUtils.setField(newTemplate, "id", 20L);
    Vehicle newVehicle = new Vehicle();
    ReflectionTestUtils.setField(newVehicle, "id", 2L);

    when(componentRepository.findById(100L)).thenReturn(Optional.of(component));
    when(componentTemplateService.findById(20L)).thenReturn(newTemplate);
    when(componentRepository.save(component)).thenReturn(component);

    Component result = componentService.editComponent(100L, newVehicle, 20L, "POOR", 80);

    assertEquals(newVehicle, result.getVehicle());
    assertEquals(newTemplate, result.getTemplate());
    assertEquals(Condition.POOR, result.getCondition());
    assertEquals(80, result.getCounter());
    verify(componentRepository).save(component);
  }

  @Test
  void deleteComponent_deletesAndReturnsComponent() {
    when(componentRepository.findById(100L)).thenReturn(Optional.of(component));

    Component result = componentService.deleteComponent(100L);

    assertSame(component, result);
    verify(componentRepository).delete(component);
  }

  @Test
  void findAllByVehicleAndTemplateMeasuringUnitType_delegatesWithEnum() {
    when(componentRepository.findAllByVehicleAndTemplateMeasuringUnitType(
            vehicle, IntervalUnit.KILOMETERS))
        .thenReturn(List.of(component));

    List<Component> result =
        componentService.findAllByVehicleAndTemplateMeasuringUnitType(vehicle, "KILOMETERS");

    assertEquals(1, result.size());
    verify(componentRepository)
        .findAllByVehicleAndTemplateMeasuringUnitType(vehicle, IntervalUnit.KILOMETERS);
  }

  @Test
  void findAllByVehicle_delegatesToRepository() {
    when(componentRepository.findAllByVehicle(vehicle)).thenReturn(List.of(component));

    assertEquals(1, componentService.findAllByVehicle(vehicle).size());
  }

  @Test
  void findAllByVehicleAndCondition_delegatesToRepository() {
    when(componentRepository.findAllByVehicleAndCondition(vehicle, Condition.GOOD))
        .thenReturn(List.of(component));

    assertEquals(1, componentService.findAllByVehicleAndCondition(vehicle, Condition.GOOD).size());
  }
}
