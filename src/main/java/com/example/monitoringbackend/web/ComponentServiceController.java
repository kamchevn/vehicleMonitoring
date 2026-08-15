package com.example.monitoringbackend.web;

import com.example.monitoringbackend.exceptions.IntervalDoesNotMatchException;
import com.example.monitoringbackend.exceptions.ServiceNotFoundException;
import com.example.monitoringbackend.exceptions.VehicleNotFoundException;
import com.example.monitoringbackend.model.Component;
import com.example.monitoringbackend.model.ComponentServiceDetail;
import com.example.monitoringbackend.model.dto.ComponentServiceRequestDto;
import com.example.monitoringbackend.model.dto.DisplayServiceDto;
import com.example.monitoringbackend.security.ResourceAccessGuard;
import com.example.monitoringbackend.service.application.ServiceApplicationService;
import com.example.monitoringbackend.service.domain.ComponentService;
import com.example.monitoringbackend.service.domain.ComponentServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/service")
@Tag(
    name = "Service API",
    description =
        "Endpoints for creating and listing vehicle regular/urgent services.") // Swagger tag
public class ComponentServiceController {
  private static final Logger LOGGER = LoggerFactory.getLogger(ComponentServiceController.class);

  private final ComponentServiceService componentServiceService;
  private final ServiceApplicationService serviceApplicationService;
  private final ComponentService componentService;
  private final ResourceAccessGuard accessGuard;

  public ComponentServiceController(
      ComponentServiceService componentServiceService,
      ServiceApplicationService serviceApplicationService,
      ComponentService componentService,
      ResourceAccessGuard accessGuard) {
    this.componentServiceService = componentServiceService;
    this.serviceApplicationService = serviceApplicationService;
    this.componentService = componentService;
    this.accessGuard = accessGuard;
  }

  @Operation(
      summary = "Returns service by id",
      description = "Finds the service by it's id and returns the service object matching.")
  @GetMapping("/{id}")
  public ResponseEntity<?> getCheckById(
      @PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
    try {
      accessGuard.requireServiceAccess(user, id);
      return ResponseEntity.ok(serviceApplicationService.findById(id));
    } catch (ServiceNotFoundException | VehicleNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
  }

  @Operation(
      summary = "List all services for user as pageable",
      description =
          "Checks the user and his role, finds the services based on his role and returns them as pageable. There can be applied vehicle and serviceType filters for the services searching.")
  @GetMapping("/page")
  public ResponseEntity<Page<DisplayServiceDto>> getComponentServicesPage(
      @AuthenticationPrincipal UserDetails user,
      @RequestParam(required = false) Long vehicleId,
      @RequestParam(required = false) String serviceType,
      @RequestParam(defaultValue = "1") String pageNum,
      @RequestParam(defaultValue = "20") String pageSize) {
    Page<DisplayServiceDto> page =
        this.serviceApplicationService.findPage(
            user,
            vehicleId,
            serviceType,
            Integer.parseInt(pageNum) - 1,
            Integer.parseInt(pageSize));
    return ResponseEntity.ok(page);
  }

  @Operation(
      summary = "Do a regular/urgent service for a vehicle",
      description =
          "Find the vehicle by it's id, finds the components selected from the form, updates their counters and conditions, as well as the condition of the vehicle, and creates a new service object in the database.")
  @PostMapping("/{id}")
  public ResponseEntity<String> serviceVehicle(
      @PathVariable Long id,
      @RequestBody ComponentServiceRequestDto request,
      @AuthenticationPrincipal UserDetails user) {

    try {
      // The path variable is the vehicle being serviced.
      accessGuard.requireVehicleAccess(user, id);
      List<ComponentServiceDetail> details =
          request.getComponentDetails().stream()
              .map(
                  dto -> {
                    // Guard each referenced component so a caller cannot service their own
                    // vehicle using component ids belonging to somebody else.
                    accessGuard.requireComponentAccess(user, dto.getComponentId());
                    Component component = componentService.findById(dto.getComponentId());
                    ComponentServiceDetail detail = new ComponentServiceDetail();
                    detail.setComponent(component);
                    detail.setCurrentCondition(dto.getCurrentCondition());
                    return detail;
                  })
              .toList();

      componentServiceService.checkConditionForVehicle(
          id, request.getServiceType(), request.getNote(), request.getCheckTime(), details);

      return ResponseEntity.ok(
          String.format("Successfully inserted new condition check for vehicle %d.", id));
    } catch (IntervalDoesNotMatchException ex) {
      LOGGER.warn("Invalid service interval submitted for vehicle {}", id, ex);
      return ResponseEntity.badRequest().body("The submitted service interval is invalid.");
    }
  }
}
