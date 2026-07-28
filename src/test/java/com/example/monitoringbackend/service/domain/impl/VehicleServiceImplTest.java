package com.example.monitoringbackend.service.domain.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.example.monitoringbackend.exceptions.VehicleNotFoundException;
import com.example.monitoringbackend.model.*;
import com.example.monitoringbackend.model.dto.InsertWindowStatusDto;
import com.example.monitoringbackend.model.enumerations.*;
import com.example.monitoringbackend.repository.ComponentServiceRepository;
import com.example.monitoringbackend.repository.IntervalInsertRepository;
import com.example.monitoringbackend.repository.VehicleRepository;
import com.example.monitoringbackend.service.domain.ComponentTemplateService;
import com.example.monitoringbackend.service.domain.UserService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class VehicleServiceImplTest {

  @Mock private VehicleRepository vehicleRepository;

  @Mock private com.example.monitoringbackend.service.domain.ComponentService componentService;

  @Mock private ComponentTemplateService componentTemplateService;

  @Mock private ComponentServiceRepository componentServiceRepository;

  @Mock private IntervalInsertRepository intervalInsertRepository;

  @Mock private InsertWindowStatusService insertWindowStatusService;

  @Mock private UserService userService;

  @InjectMocks private VehicleServiceImpl vehicleService;

  private User owner;
  private Vehicle vehicle;
  private InsertWindowStatusDto windowStatus;

  @BeforeEach
  void setUp() {
    owner = new User("alice", "encoded", "alice@example.com", "Alice", "Smith", Role.ROLE_USER);
    vehicle =
        new Vehicle(
            "Civic",
            2020,
            50000,
            VehicleType.CAR,
            VehicleFuelType.PETROL,
            null,
            null,
            Condition.GOOD,
            IntervalInsertPeriod.WEEKLY,
            owner);
    ReflectionTestUtils.setField(vehicle, "id", 1L);

    windowStatus = new InsertWindowStatusDto(true, true, true, false, false, false, false, false);
  }

  private UserDetails userDetails(String username, String role) {
    return org.springframework.security.core.userdetails.User.withUsername(username)
        .password("x")
        .authorities(Collections.singletonList(new SimpleGrantedAuthority(role)))
        .build();
  }

  @Test
  void findById_returnsVehicleWithInsertWindowStatus() {
    when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
    when(insertWindowStatusService.calculate(eq(vehicle), any())).thenReturn(windowStatus);

    Vehicle result = vehicleService.findById(1L);

    assertEquals(1L, result.getId());
    assertSame(windowStatus, result.getInsertWindowStatus());
  }

  @Test
  void findById_throws_whenMissing() {
    when(vehicleRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(VehicleNotFoundException.class, () -> vehicleService.findById(99L));
  }

  @Test
  void findAll_adminSeesAllVehicles() {
    when(vehicleRepository.findAll()).thenReturn(List.of(vehicle));
    when(insertWindowStatusService.calculate(eq(vehicle), any())).thenReturn(windowStatus);

    List<Vehicle> result = vehicleService.findAll(userDetails("admin", "ROLE_ADMIN"));

    assertEquals(1, result.size());
    verify(vehicleRepository).findAll();
    verify(vehicleRepository, never()).findAllByUserUsername(any());
  }

  @Test
  void findAll_userSeesOnlyOwnVehicles() {
    when(vehicleRepository.findAllByUserUsername("alice")).thenReturn(List.of(vehicle));
    when(insertWindowStatusService.calculate(eq(vehicle), any())).thenReturn(windowStatus);

    List<Vehicle> result = vehicleService.findAll(userDetails("alice", "ROLE_USER"));

    assertEquals(1, result.size());
    verify(vehicleRepository).findAllByUserUsername("alice");
  }

  @Test
  void createNewVehicle_savesVehicleForUser() {
    when(userService.getUser("alice")).thenReturn(owner);
    when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(inv -> inv.getArgument(0));

    Vehicle result =
        vehicleService.createNewVehicle(
            "Civic",
            2020,
            50000,
            "CAR",
            "PETROL",
            null,
            null,
            "GOOD",
            "WEEKLY",
            userDetails("alice", "ROLE_USER"));

    assertEquals("Civic", result.getName());
    assertEquals(VehicleType.CAR, result.getType());
    assertEquals(VehicleFuelType.PETROL, result.getFuelType());
    assertEquals(Condition.GOOD, result.getCondition());
    assertEquals(owner, result.getUser());
    verify(vehicleRepository).save(any(Vehicle.class));
  }

  @Test
  void deleteVehicle_removesChecksComponentsAndInserts() {
    when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
    when(insertWindowStatusService.calculate(eq(vehicle), any())).thenReturn(windowStatus);

    ComponentService check = new ComponentService();
    when(componentServiceRepository.findAllByVehicle(vehicle)).thenReturn(List.of(check));

    Component component = new Component(vehicle, new ComponentTemplate(), Condition.GOOD, 0);
    ReflectionTestUtils.setField(component, "id", 100L);
    when(componentService.findAllByVehicle(vehicle)).thenReturn(List.of(component));
    when(componentService.deleteComponent(100L)).thenReturn(component);

    IntervalInsert insert = new IntervalInsert();
    when(intervalInsertRepository.findAllByVehicle(vehicle)).thenReturn(List.of(insert));

    Vehicle result = vehicleService.deleteVehicle(1L);

    assertSame(vehicle, result);
    verify(componentServiceRepository).delete(check);
    verify(componentService).deleteComponent(100L);
    verify(intervalInsertRepository).deleteAll(List.of(insert));
    verify(vehicleRepository).delete(vehicle);
  }

  @Test
  void insertDistanceOrFuelForVehicle_updatesKmCountersAndRecordsInsert() {
    when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
    when(insertWindowStatusService.calculate(eq(vehicle), any())).thenReturn(windowStatus);

    ComponentTemplate template = new ComponentTemplate();
    template.setWarningInterval(5000);
    template.setMinCheckInterval(10000);
    Component component = new Component(vehicle, template, Condition.VERY_GOOD, 100);
    component.setNeedsCheck(false);
    when(componentService.findAllByVehicleAndTemplateMeasuringUnitType(vehicle, "KILOMETERS"))
        .thenReturn(List.of(component));
    when(componentService.findAllByVehicle(vehicle)).thenReturn(List.of(component));
    when(componentService.saveAll(any())).thenAnswer(inv -> inv.getArgument(0));
    when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(inv -> inv.getArgument(0));

    LocalDateTime insertTime = LocalDateTime.of(2026, 7, 11, 10, 0);
    vehicleService.insertDistanceOrFuelForVehicle(1L, "KILOMETERS", 200, insertTime, false);

    assertEquals(300, component.getCounter());
    assertEquals(50200, vehicle.getTotalKilometers());
    assertEquals(insertTime, vehicle.getLastInsertKilometers());
    verify(intervalInsertRepository).save(any(IntervalInsert.class));
    verify(vehicleRepository, atLeastOnce()).save(vehicle);
  }

  @Test
  void changeVehicleCondition_setsVehicleBasedOnComponentThresholds() {
    when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
    when(insertWindowStatusService.calculate(eq(vehicle), any())).thenReturn(windowStatus);

    ComponentTemplate template = new ComponentTemplate();
    template.setWarningInterval(5000);
    template.setMinCheckInterval(10000);

    Component c1 = new Component(vehicle, template, Condition.VERY_GOOD, 1000);
    Component c2 = new Component(vehicle, template, Condition.VERY_GOOD, 2000);
    Component c3 = new Component(vehicle, template, Condition.VERY_GOOD, 3000);
    when(componentService.findAllByVehicle(vehicle)).thenReturn(List.of(c1, c2, c3));
    when(componentService.saveAll(any())).thenAnswer(inv -> inv.getArgument(0));
    when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

    vehicleService.changeVehicleCondition(1L);

    assertEquals(Condition.VERY_GOOD, vehicle.getCondition());
    assertEquals(Condition.VERY_GOOD, c1.getCondition());
    verify(componentService).saveAll(any());
    verify(vehicleRepository).save(vehicle);
  }

  @Test
  void changeVehicleCondition_setsUnknown_whenAnyComponentUnknown() {
    when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
    when(insertWindowStatusService.calculate(eq(vehicle), any())).thenReturn(windowStatus);

    ComponentTemplate template = new ComponentTemplate();
    template.setWarningInterval(5000);
    template.setMinCheckInterval(10000);

    Component known = new Component(vehicle, template, Condition.GOOD, 6000);
    Component unknown = new Component(vehicle, template, Condition.UNKNOWN, 0);
    when(componentService.findAllByVehicle(vehicle)).thenReturn(List.of(known, unknown));
    when(componentService.saveAll(any())).thenAnswer(inv -> inv.getArgument(0));
    when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

    vehicleService.changeVehicleCondition(1L);

    assertEquals(Condition.UNKNOWN, vehicle.getCondition());
  }

  @Test
  void editVehicle_swapsSparkPlugForGlowPlug_whenFuelTypeChangesToDiesel() {
    when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
    when(insertWindowStatusService.calculate(eq(vehicle), any())).thenReturn(windowStatus);
    when(userService.getUser("alice")).thenReturn(owner);

    ComponentTemplate sparkTemplate = new ComponentTemplate();
    sparkTemplate.setComponentType(ComponentType.SPARK_PLUG);
    sparkTemplate.setWarningInterval(5000);
    sparkTemplate.setMinCheckInterval(10000);
    Component spark = new Component(vehicle, sparkTemplate, Condition.GOOD, 0);
    ReflectionTestUtils.setField(spark, "id", 50L);

    ComponentTemplate glowTemplate = new ComponentTemplate();
    glowTemplate.setComponentType(ComponentType.GLOW_PLUG);
    glowTemplate.setWarningInterval(5000);
    glowTemplate.setMinCheckInterval(10000);

    when(componentService.findAllByVehicle(vehicle)).thenReturn(List.of(spark));
    when(componentService.deleteComponent(50L)).thenReturn(spark);
    when(componentTemplateService.getTemplatesForVehicleTypeAndComponentType(
            VehicleType.CAR, ComponentType.GLOW_PLUG))
        .thenReturn(List.of(glowTemplate));
    when(componentService.save(any(Component.class))).thenAnswer(inv -> inv.getArgument(0));
    when(componentService.saveAll(any())).thenAnswer(inv -> inv.getArgument(0));
    when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(inv -> inv.getArgument(0));

    Vehicle result =
        vehicleService.editVehicle(
            1L,
            "Civic",
            2020,
            50000,
            "CAR",
            "DIESEL",
            null,
            null,
            "GOOD",
            "WEEKLY",
            userDetails("alice", "ROLE_USER"));

    assertEquals(VehicleFuelType.DIESEL, result.getFuelType());
    verify(componentService).deleteComponent(50L);
    ArgumentCaptor<Component> captor = ArgumentCaptor.forClass(Component.class);
    verify(componentService).save(captor.capture());
    assertEquals(ComponentType.GLOW_PLUG, captor.getValue().getTemplate().getComponentType());
  }
}
