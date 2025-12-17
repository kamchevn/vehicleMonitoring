package com.example.monitoringbackend.web;

import com.example.monitoringbackend.exceptions.IntervalDoesNotMatchException;
import com.example.monitoringbackend.model.ConditionCheck;
import com.example.monitoringbackend.service.ConditionCheckService;
import com.example.monitoringbackend.service.VehicleService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/conditionCheck")
public class ConditionCheckController {
    private final ConditionCheckService conditionCheckService;
    private final VehicleService vehicleService;

    public ConditionCheckController(ConditionCheckService conditionCheckService, VehicleService vehicleService) {
        this.conditionCheckService = conditionCheckService;
        this.vehicleService = vehicleService;
    }

    @GetMapping("/page")
    public ResponseEntity<Page<ConditionCheck>> getProductPage(
            @RequestParam(required = false) String vehicleInternalCode,
            @RequestParam(required = false) String checkType,
            @RequestParam(defaultValue = "1") String pageNum,
            @RequestParam(defaultValue = "20") String pageSize
    ) {
        Page<ConditionCheck> page = this.conditionCheckService.findPage(vehicleInternalCode, checkType, Integer.parseInt(pageNum)-1, Integer.parseInt(pageSize));
        return ResponseEntity.ok(page);
    }
    @PostMapping("/{internalCode}")
    public ResponseEntity<String> checkConditionForVehicle(@PathVariable String internalCode,
                                                           @RequestParam String checkType,
                                                           @RequestParam String previousCondition,
                                                           @RequestParam String currentCondition,
                                                           @RequestParam String note,
                                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime checkTime){
        try {
            vehicleService.checkConditionForVehicle(internalCode,checkType,previousCondition,currentCondition,note,checkTime);
            return ResponseEntity.ok(String.format("Successfully inserted new condition check for vehicle %s.", internalCode));
        }
        catch (IntervalDoesNotMatchException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
