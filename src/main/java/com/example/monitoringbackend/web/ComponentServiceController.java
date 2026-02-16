package com.example.monitoringbackend.web;

import com.example.monitoringbackend.exceptions.IntervalDoesNotMatchException;
import com.example.monitoringbackend.exceptions.VehicleNotFoundException;
import com.example.monitoringbackend.model.Component;
import com.example.monitoringbackend.model.ComponentServiceDetail;
import com.example.monitoringbackend.model.dto.ComponentServiceRequestDto;
import com.example.monitoringbackend.model.dto.DisplayServiceDto;
import com.example.monitoringbackend.service.application.ServiceApplicationService;
import com.example.monitoringbackend.service.domain.ComponentServiceService;
import com.example.monitoringbackend.service.domain.ComponentService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/service")
public class ComponentServiceController {
    private final ComponentServiceService componentServiceService;
    private final ServiceApplicationService serviceApplicationService;
    private final ComponentService componentService;

    public ComponentServiceController(ComponentServiceService componentServiceService, ServiceApplicationService serviceApplicationService, ComponentService componentService) {
        this.componentServiceService = componentServiceService;
        this.serviceApplicationService = serviceApplicationService;
        this.componentService = componentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCheckById (@PathVariable Long id){
        try {
            return ResponseEntity.ok(serviceApplicationService.findById(id));
        } catch (VehicleNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/page")
    public ResponseEntity<Page<DisplayServiceDto>> getComponentServicesPage(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam(required = false) Long vehicleId,
            @RequestParam(required = false) String serviceType,
            @RequestParam(defaultValue = "1") String pageNum,
            @RequestParam(defaultValue = "20") String pageSize
    ) {
        Page<DisplayServiceDto> page = this.serviceApplicationService.findPage(user,vehicleId, serviceType, Integer.parseInt(pageNum)-1, Integer.parseInt(pageSize));
        return ResponseEntity.ok(page);
    }
    @PostMapping("/{id}")
    public ResponseEntity<String> serviceVehicle(
            @PathVariable Long id,
            @RequestBody ComponentServiceRequestDto request) {

        try {
            List<ComponentServiceDetail> details = request.getComponentDetails().stream().map(dto -> {
                Component component = componentService.findById(dto.getComponentId());
                ComponentServiceDetail detail = new ComponentServiceDetail();
                detail.setComponent(component);
                detail.setCurrentCondition(dto.getCurrentCondition());
                return detail;
            }).toList();

            componentServiceService.checkConditionForVehicle(
                    id,
                    request.getServiceType(),
                    request.getNote(),
                    request.getCheckTime(),
                    details
            );

            return ResponseEntity.ok(
                    String.format("Successfully inserted new condition check for vehicle %d.", id)
            );
        } catch (IntervalDoesNotMatchException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
