package com.example.monitoringbackend.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.monitoringbackend.exceptions.ComponentNotFoundException;
import com.example.monitoringbackend.exceptions.ServiceNotFoundException;
import com.example.monitoringbackend.exceptions.VehicleNotFoundException;
import com.example.monitoringbackend.model.Component;
import com.example.monitoringbackend.model.ComponentService;
import com.example.monitoringbackend.model.User;
import com.example.monitoringbackend.model.Vehicle;
import com.example.monitoringbackend.model.enumerations.Condition;
import com.example.monitoringbackend.model.enumerations.IntervalInsertPeriod;
import com.example.monitoringbackend.model.enumerations.Role;
import com.example.monitoringbackend.model.enumerations.VehicleType;
import com.example.monitoringbackend.repository.ComponentRepository;
import com.example.monitoringbackend.repository.ComponentServiceRepository;
import com.example.monitoringbackend.repository.VehicleRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class ResourceAccessGuardTest {

  @Mock private VehicleRepository vehicleRepository;
  @Mock private ComponentRepository componentRepository;
  @Mock private ComponentServiceRepository componentServiceRepository;

  private ResourceAccessGuard accessGuard;

  private User owner;
  private User intruder;
  private User admin;
  private Vehicle ownedVehicle;

  @BeforeEach
  void setUp() {
    accessGuard =
        new ResourceAccessGuard(vehicleRepository, componentRepository, componentServiceRepository);
    owner = user("owner", Role.ROLE_USER);
    intruder = user("intruder", Role.ROLE_USER);
    admin = user("admin", Role.ROLE_ADMIN);
    ownedVehicle = vehicleOf(owner);
  }

  private static User user(String username, Role role) {
    User user = new User(username, "encoded", username + "@example.com", "N", "S", role);
    user.setEnabled(true);
    return user;
  }

  private static Vehicle vehicleOf(User user) {
    return new Vehicle(
        "Golf",
        2016,
        120000,
        VehicleType.CAR,
        null,
        null,
        null,
        Condition.GOOD,
        IntervalInsertPeriod.WEEKLY,
        user);
  }

  @Test
  void owner_mayAccessOwnVehicle() {
    when(vehicleRepository.findById(1L)).thenReturn(Optional.of(ownedVehicle));

    assertDoesNotThrow(() -> accessGuard.requireVehicleAccess(owner, 1L));
  }

  @Test
  void otherUser_mayNotAccessSomebodyElsesVehicle() {
    when(vehicleRepository.findById(1L)).thenReturn(Optional.of(ownedVehicle));

    assertThrows(AccessDeniedException.class, () -> accessGuard.requireVehicleAccess(intruder, 1L));
  }

  @Test
  void admin_mayAccessAnyVehicle() {
    when(vehicleRepository.findById(1L)).thenReturn(Optional.of(ownedVehicle));

    assertDoesNotThrow(() -> accessGuard.requireVehicleAccess(admin, 1L));
  }

  @Test
  void unauthenticatedCaller_isDenied() {
    when(vehicleRepository.findById(1L)).thenReturn(Optional.of(ownedVehicle));

    assertThrows(AccessDeniedException.class, () -> accessGuard.requireVehicleAccess(null, 1L));
  }

  @Test
  void missingVehicle_isReportedAsNotFound() {
    when(vehicleRepository.findById(404L)).thenReturn(Optional.empty());

    assertThrows(
        VehicleNotFoundException.class, () -> accessGuard.requireVehicleAccess(owner, 404L));
  }

  @Test
  void nullVehicleId_isDenied() {
    assertThrows(AccessDeniedException.class, () -> accessGuard.requireVehicleAccess(owner, null));
  }

  @Test
  void componentAccess_followsTheOwningVehicle() {
    Component component = new Component(ownedVehicle, null, Condition.GOOD, 0);
    when(componentRepository.findById(7L)).thenReturn(Optional.of(component));

    assertDoesNotThrow(() -> accessGuard.requireComponentAccess(owner, 7L));
    assertThrows(
        AccessDeniedException.class, () -> accessGuard.requireComponentAccess(intruder, 7L));
  }

  @Test
  void missingComponent_isReportedAsNotFound() {
    when(componentRepository.findById(404L)).thenReturn(Optional.empty());

    assertThrows(
        ComponentNotFoundException.class, () -> accessGuard.requireComponentAccess(owner, 404L));
  }

  @Test
  void serviceAccess_followsTheOwningVehicle() {
    ComponentService service = new ComponentService();
    service.setVehicle(ownedVehicle);
    when(componentServiceRepository.findById(9L)).thenReturn(Optional.of(service));

    assertDoesNotThrow(() -> accessGuard.requireServiceAccess(owner, 9L));
    assertThrows(AccessDeniedException.class, () -> accessGuard.requireServiceAccess(intruder, 9L));
  }

  @Test
  void missingService_isReportedAsNotFound() {
    when(componentServiceRepository.findById(404L)).thenReturn(Optional.empty());

    assertThrows(
        ServiceNotFoundException.class, () -> accessGuard.requireServiceAccess(owner, 404L));
  }

  @Test
  void resourceWithoutAnOwner_isDenied() {
    Component orphan = new Component(null, null, Condition.GOOD, 0);
    when(componentRepository.findById(5L)).thenReturn(Optional.of(orphan));

    assertThrows(AccessDeniedException.class, () -> accessGuard.requireComponentAccess(owner, 5L));
  }

  @Test
  void isAdmin_reflectsGrantedAuthorities() {
    assertTrue(accessGuard.isAdmin(admin));
    assertFalse(accessGuard.isAdmin(owner));
    assertFalse(accessGuard.isAdmin((UserDetails) null));
  }
}
