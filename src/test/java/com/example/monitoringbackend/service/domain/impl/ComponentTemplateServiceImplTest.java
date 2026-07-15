package com.example.monitoringbackend.service.domain.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.monitoringbackend.model.ComponentTemplate;
import com.example.monitoringbackend.model.enumerations.ComponentType;
import com.example.monitoringbackend.model.enumerations.IntervalUnit;
import com.example.monitoringbackend.model.enumerations.VehicleType;
import com.example.monitoringbackend.repository.ComponentTemplateRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ComponentTemplateServiceImplTest {

  @Mock private ComponentTemplateRepository repository;

  @InjectMocks private ComponentTemplateServiceImpl service;

  private ComponentTemplate template;

  @BeforeEach
  void setUp() {
    template =
        new ComponentTemplate(
            "Oil Filter",
            VehicleType.CAR,
            ComponentType.OIL_FILTER,
            IntervalUnit.KILOMETERS,
            10000,
            8000);
    ReflectionTestUtils.setField(template, "id", 1L);
  }

  @Test
  void findById_returnsTemplate_whenPresent() {
    when(repository.findById(1L)).thenReturn(Optional.of(template));

    ComponentTemplate result = service.findById(1L);

    assertEquals(1L, result.getId());
    assertEquals("Oil Filter", result.getName());
  }

  @Test
  void findById_throws_whenMissing() {
    when(repository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class, () -> service.findById(99L));
  }

  @Test
  void getTemplatesForVehicleType_delegatesToRepository() {
    when(repository.findAllByVehicleType(VehicleType.CAR)).thenReturn(List.of(template));

    List<ComponentTemplate> result = service.getTemplatesForVehicleType(VehicleType.CAR);

    assertEquals(1, result.size());
    verify(repository).findAllByVehicleType(VehicleType.CAR);
  }

  @Test
  void getTemplatesForVehicleTypeAndComponentType_delegatesToRepository() {
    when(repository.findAllByVehicleTypeAndComponentType(VehicleType.CAR, ComponentType.OIL_FILTER))
        .thenReturn(List.of(template));

    List<ComponentTemplate> result =
        service.getTemplatesForVehicleTypeAndComponentType(
            VehicleType.CAR, ComponentType.OIL_FILTER);

    assertEquals(1, result.size());
  }

  @Test
  void updateRules_updatesIntervals_whenValid() {
    when(repository.findById(1L)).thenReturn(Optional.of(template));
    when(repository.save(template)).thenReturn(template);

    ComponentTemplate result = service.updateRules(1L, 12000, 9000);

    assertEquals(12000, result.getMinCheckInterval());
    assertEquals(9000, result.getWarningInterval());
    verify(repository).save(template);
  }

  @Test
  void updateRules_throws_whenWarningNotLessThanMin() {
    when(repository.findById(1L)).thenReturn(Optional.of(template));

    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class, () -> service.updateRules(1L, 8000, 8000));

    assertTrue(ex.getMessage().contains("Warning interval must be less than minCheckInterval"));
    verify(repository, never()).save(any());
  }

  @Test
  void updateRules_throws_whenTemplateMissing() {
    when(repository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class, () -> service.updateRules(1L, 10000, 8000));
  }
}
