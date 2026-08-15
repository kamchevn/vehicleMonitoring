package com.example.monitoringbackend.security;

import com.example.monitoringbackend.exceptions.ComponentNotFoundException;
import com.example.monitoringbackend.exceptions.ServiceNotFoundException;
import com.example.monitoringbackend.exceptions.VehicleNotFoundException;
import com.example.monitoringbackend.model.Component;
import com.example.monitoringbackend.model.ComponentService;
import com.example.monitoringbackend.model.Vehicle;
import com.example.monitoringbackend.model.enumerations.Role;
import com.example.monitoringbackend.repository.ComponentRepository;
import com.example.monitoringbackend.repository.ComponentServiceRepository;
import com.example.monitoringbackend.repository.VehicleRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Single point of object-level authorization for the resources that hang off a {@link Vehicle}.
 *
 * <p>Every endpoint that accepts a caller-supplied identifier must call one of the {@code
 * require*Access} methods before acting on it. Reads go through the repositories rather than the
 * domain services on purpose: {@code VehicleService.findById} recomputes insert-window state and
 * can write to the vehicle, which an authorization check must never do.
 *
 * <p>A missing resource keeps its existing 404; a resource owned by somebody else is a 403.
 */
@Service
public class ResourceAccessGuard {

  private final VehicleRepository vehicleRepository;
  private final ComponentRepository componentRepository;
  private final ComponentServiceRepository componentServiceRepository;

  public ResourceAccessGuard(
      VehicleRepository vehicleRepository,
      ComponentRepository componentRepository,
      ComponentServiceRepository componentServiceRepository) {
    this.vehicleRepository = vehicleRepository;
    this.componentRepository = componentRepository;
    this.componentServiceRepository = componentServiceRepository;
  }

  /** Asserts the caller may act on the given vehicle. */
  public void requireVehicleAccess(UserDetails principal, Long vehicleId) {
    if (vehicleId == null) {
      throw new AccessDeniedException("No vehicle supplied.");
    }
    Vehicle vehicle =
        vehicleRepository
            .findById(vehicleId)
            .orElseThrow(() -> new VehicleNotFoundException(vehicleId));
    requireOwnership(principal, vehicle);
  }

  /** Asserts the caller may act on the vehicle that owns the given component. */
  public void requireComponentAccess(UserDetails principal, Long componentId) {
    if (componentId == null) {
      throw new AccessDeniedException("No component supplied.");
    }
    Component component =
        componentRepository
            .findById(componentId)
            .orElseThrow(() -> new ComponentNotFoundException(componentId));
    requireOwnership(principal, component.getVehicle());
  }

  /** Asserts the caller may act on the vehicle that owns the given service record. */
  public void requireServiceAccess(UserDetails principal, Long serviceId) {
    if (serviceId == null) {
      throw new AccessDeniedException("No service supplied.");
    }
    ComponentService service =
        componentServiceRepository
            .findById(serviceId)
            .orElseThrow(() -> new ServiceNotFoundException(serviceId));
    requireOwnership(principal, service.getVehicle());
  }

  public boolean isAdmin(UserDetails principal) {
    return principal != null
        && principal.getAuthorities().stream()
            .anyMatch(authority -> Role.ROLE_ADMIN.name().equals(authority.getAuthority()));
  }

  private void requireOwnership(UserDetails principal, Vehicle vehicle) {
    if (principal == null) {
      throw new AccessDeniedException("Unauthenticated request.");
    }
    if (isAdmin(principal)) {
      return;
    }
    if (vehicle == null || vehicle.getUser() == null) {
      throw new AccessDeniedException("Resource has no owner.");
    }
    if (!vehicle.getUser().getUsername().equals(principal.getUsername())) {
      throw new AccessDeniedException("Resource belongs to another user.");
    }
  }
}
