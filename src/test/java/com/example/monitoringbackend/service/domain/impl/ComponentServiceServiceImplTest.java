package com.example.monitoringbackend.service.domain.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.monitoringbackend.exceptions.ServiceNotFoundException;
import com.example.monitoringbackend.model.*;
import com.example.monitoringbackend.model.enumerations.Condition;
import com.example.monitoringbackend.model.enumerations.IntervalUnit;
import com.example.monitoringbackend.model.enumerations.ServiceType;
import com.example.monitoringbackend.repository.ComponentServiceRepository;
import com.example.monitoringbackend.service.domain.VehicleService;
import java.time.LocalDateTime;
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
class ComponentServiceServiceImplTest {

  @Mock private ComponentServiceRepository componentServiceRepository;

  @Mock private VehicleService vehicleService;

  @Mock private com.example.monitoringbackend.service.domain.ComponentService componentService;

  @InjectMocks private ComponentServiceServiceImpl service;

  private Vehicle vehicle;
  private ComponentTemplate template;
  private Component component;

  @BeforeEach
  void setUp() {
    vehicle = new Vehicle();
    ReflectionTestUtils.setField(vehicle, "id", 1L);
    vehicle.setCondition(Condition.GOOD);

    template = new ComponentTemplate();
    template.setWarningInterval(5000);
    template.setMinCheckInterval(10000);
    template.setMeasuringUnitType(IntervalUnit.KILOMETERS);
    ReflectionTestUtils.setField(template, "id", 10L);

    component = new Component(vehicle, template, Condition.GOOD, 6000);
    ReflectionTestUtils.setField(component, "id", 100L);
  }

  @Test
  void findById_returnsService_whenPresent() {
    ComponentService check = new ComponentService();
    ReflectionTestUtils.setField(check, "id", 5L);
    when(componentServiceRepository.findById(5L)).thenReturn(Optional.of(check));

    ComponentService result = service.findById(5L);

    assertEquals(5L, result.getId());
  }

  @Test
  void findById_throws_whenMissing() {
    when(componentServiceRepository.findById(5L)).thenReturn(Optional.empty());

    assertThrows(ServiceNotFoundException.class, () -> service.findById(5L));
  }

  @Test
  void checkConditionForVehicle_updatesComponentAndSavesService() {
    when(vehicleService.findById(1L)).thenReturn(vehicle);
    when(componentService.findById(100L)).thenReturn(component);
    when(componentService.save(any(Component.class))).thenAnswer(inv -> inv.getArgument(0));
    when(componentServiceRepository.save(any(ComponentService.class)))
        .thenAnswer(inv -> inv.getArgument(0));

    ComponentServiceDetail detail = new ComponentServiceDetail();
    detail.setComponent(component);
    detail.setCurrentCondition(Condition.VERY_GOOD);

    LocalDateTime checkTime = LocalDateTime.of(2026, 7, 13, 10, 0);
    service.checkConditionForVehicle(1L, "REGULAR", "Oil change", checkTime, List.of(detail));

    assertEquals(Condition.VERY_GOOD, component.getCondition());
    assertEquals(0, component.getCounter());
    assertFalse(component.isWarningFlag());
    assertFalse(component.isNeedsCheck());
    assertNotNull(component.getLastChecked());
    assertEquals(Condition.GOOD, detail.getPreviousCondition());

    verify(componentService).save(component);
    verify(vehicleService).changeVehicleCondition(1L);

    ArgumentCaptor<ComponentService> captor = ArgumentCaptor.forClass(ComponentService.class);
    verify(componentServiceRepository).save(captor.capture());
    ComponentService saved = captor.getValue();
    assertEquals(ServiceType.REGULAR, saved.getServiceType());
    assertEquals(Condition.GOOD, saved.getPreviousCondition());
    assertEquals(Condition.GOOD, saved.getCurrentCondition());
    assertEquals("Oil change", saved.getNote());
    assertEquals(1, saved.getComponentDetails().size());
  }

  @Test
  void checkConditionForVehicle_setsCounterForPoorCondition() {
    when(vehicleService.findById(1L)).thenReturn(vehicle);
    when(componentService.findById(100L)).thenReturn(component);
    when(componentService.save(any(Component.class))).thenAnswer(inv -> inv.getArgument(0));
    when(componentServiceRepository.save(any(ComponentService.class)))
        .thenAnswer(inv -> inv.getArgument(0));

    ComponentServiceDetail detail = new ComponentServiceDetail();
    detail.setComponent(component);
    detail.setCurrentCondition(Condition.POOR);

    service.checkConditionForVehicle(1L, "URGENT", "  ", LocalDateTime.now(), List.of(detail));

    assertEquals(Condition.POOR, component.getCondition());
    assertEquals(10000, component.getCounter());
    assertTrue(component.isNeedsCheck());
    assertFalse(component.isWarningFlag());

    ArgumentCaptor<ComponentService> captor = ArgumentCaptor.forClass(ComponentService.class);
    verify(componentServiceRepository).save(captor.capture());
    assertNull(captor.getValue().getNote());
    assertEquals(ServiceType.URGENT, captor.getValue().getServiceType());
  }

  @Test
  void checkConditionForVehicle_setsCounterForGoodCondition() {
    when(vehicleService.findById(1L)).thenReturn(vehicle);
    when(componentService.findById(100L)).thenReturn(component);
    when(componentService.save(any(Component.class))).thenAnswer(inv -> inv.getArgument(0));
    when(componentServiceRepository.save(any(ComponentService.class)))
        .thenAnswer(inv -> inv.getArgument(0));

    ComponentServiceDetail detail = new ComponentServiceDetail();
    detail.setComponent(component);
    detail.setCurrentCondition(Condition.GOOD);

    service.checkConditionForVehicle(1L, "REGULAR", null, LocalDateTime.now(), List.of(detail));

    assertEquals(5000, component.getCounter());
    assertTrue(component.isWarningFlag());
    assertFalse(component.isNeedsCheck());
  }
}
