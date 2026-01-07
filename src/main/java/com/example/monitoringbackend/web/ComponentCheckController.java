package com.example.monitoringbackend.web;

import com.example.monitoringbackend.exceptions.IntervalDoesNotMatchException;
import com.example.monitoringbackend.model.Component;
import com.example.monitoringbackend.model.ComponentCheck;
import com.example.monitoringbackend.model.ComponentCheckDetail;
import com.example.monitoringbackend.model.dto.ComponentCheckRequestDto;
import com.example.monitoringbackend.service.ComponentCheckService;
import com.example.monitoringbackend.service.ComponentService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/conditionCheck")
public class ComponentCheckController {
    private final ComponentCheckService componentCheckService;

    private final ComponentService componentService;

    public ComponentCheckController(ComponentCheckService componentCheckService, ComponentService componentService) {
        this.componentCheckService = componentCheckService;
        this.componentService = componentService;
    }

    @GetMapping("/page")
    public ResponseEntity<Page<ComponentCheck>> getProductPage(
            @RequestParam(required = false) Long vehicleId,
            @RequestParam(required = false) String checkType,
            @RequestParam(defaultValue = "1") String pageNum,
            @RequestParam(defaultValue = "20") String pageSize
    ) {
        Page<ComponentCheck> page = this.componentCheckService.findPage(vehicleId, checkType, Integer.parseInt(pageNum)-1, Integer.parseInt(pageSize));
        return ResponseEntity.ok(page);
    }
    @PostMapping("/{id}")
    public ResponseEntity<String> checkConditionForVehicle(
            @PathVariable Long id,
            @RequestBody ComponentCheckRequestDto request) {

        try {
            List<ComponentCheckDetail> details = request.getComponentDetails().stream().map(dto -> {
                Component component = componentService.findById(dto.getComponentId());
                ComponentCheckDetail detail = new ComponentCheckDetail();
                detail.setComponent(component);
                detail.setCurrentCondition(dto.getCurrentCondition());
                return detail;
            }).toList();

            componentCheckService.checkConditionForVehicle(
                    id,
                    request.getCheckType(),
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
