package com.example.monitoringbackend.web;

import com.example.monitoringbackend.exceptions.VehicleNotFoundException;
import com.example.monitoringbackend.model.Component;
import com.example.monitoringbackend.model.enumerations.Condition;
import com.example.monitoringbackend.model.Vehicle;
import com.example.monitoringbackend.model.dto.DisplayComponentDto;
import com.example.monitoringbackend.service.application.ComponentApplicationService;
import com.example.monitoringbackend.service.domain.ComponentService;
import com.example.monitoringbackend.service.domain.VehicleService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/component")
public class ComponentController {
    private final ComponentApplicationService componentService;
    private final VehicleService vehicleService;

    public ComponentController(ComponentApplicationService componentService, VehicleService vehicleService) {
        this.componentService = componentService;
        this.vehicleService = vehicleService;
    }

    @GetMapping
    private List<DisplayComponentDto> getComponents(){
        return componentService.findAll();
    }

    @GetMapping("/page")
    public ResponseEntity<Page<DisplayComponentDto>> getComponentPage(
            @RequestParam String vehicleId,
            @RequestParam(required = false) String measuringUnit,
            @RequestParam(required = false) String condition,
            @RequestParam(defaultValue = "1") String pageNum,
            @RequestParam(defaultValue = "4") String pageSize
    ) {
        Long id = Long.parseLong(vehicleId);

        List<String> conditions = null;
        if (condition != null && !condition.isBlank()) {
            conditions = List.of(condition.split(","));
        }
        Page<DisplayComponentDto> page = this.componentService.findPage(id, measuringUnit, conditions, Integer.parseInt(pageNum)-1, Integer.parseInt(pageSize));
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getComponentById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(componentService.findById(id));
        } catch (VehicleNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<DisplayComponentDto> createComponent(
            @RequestParam Long vehicleId,
            @RequestParam Long componentTemplateId,
            @RequestParam String condition,
            @RequestParam String counter){
        Vehicle vehicle = vehicleService.findById(vehicleId);
        return ResponseEntity.ok(componentService.createNewComponent(vehicle,componentTemplateId,condition,Integer.parseInt(counter)));
    }
    @PostMapping("/edit/{id}")
    public ResponseEntity<?> editComponent(
            @PathVariable Long id,
            @RequestParam Long vehicleId,
            @RequestParam Long componentTemplateId,
            @RequestParam String condition,
            @RequestParam String counter){
        try {
            Vehicle vehicle = vehicleService.findById(vehicleId);
            return ResponseEntity.ok(componentService.editComponent(id,vehicle,componentTemplateId,condition,Integer.parseInt(counter)));
        } catch (VehicleNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteComponent(@PathVariable Long id){
        try {
            return ResponseEntity.ok(componentService.deleteComponent(id));
        } catch (VehicleNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
    @GetMapping("/findByVehicle/{vehicleId}")
    public List<DisplayComponentDto> findAllByVehicle(@PathVariable Long vehicleId) {
        Vehicle vehicle = vehicleService.findById(vehicleId);
        List<DisplayComponentDto> components = componentService.findAllByVehicle(vehicle);
        return components;
    }

    @GetMapping("/findByVehicleAndCondition/{vehicleId}/{condition}")
    public List<DisplayComponentDto> findAllByVehicleAndCondition(@PathVariable Long vehicleId, @PathVariable String condition) {
        Condition con = Condition.valueOf(condition);
        Vehicle vehicle = vehicleService.findById(vehicleId);
        List<DisplayComponentDto> components = componentService.findAllByVehicleAndCondition(vehicle,con);
        return components;
    }
}
