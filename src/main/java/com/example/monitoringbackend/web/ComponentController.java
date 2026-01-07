package com.example.monitoringbackend.web;

import com.example.monitoringbackend.exceptions.VehicleNotFoundException;
import com.example.monitoringbackend.model.Component;
import com.example.monitoringbackend.model.Condition;
import com.example.monitoringbackend.model.Vehicle;
import com.example.monitoringbackend.model.dto.DisplayComponentDto;
import com.example.monitoringbackend.service.ComponentService;
import com.example.monitoringbackend.service.VehicleService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/component")
public class ComponentController {
    private final ComponentService componentService;
    private final VehicleService vehicleService;

    public ComponentController(ComponentService componentService, VehicleService vehicleService) {
        this.componentService = componentService;
        this.vehicleService = vehicleService;
    }

    @GetMapping
    private List<Component> getComponents(){
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
        Page<Component> page = this.componentService.findPage(id, measuringUnit, condition, Integer.parseInt(pageNum)-1, Integer.parseInt(pageSize));
        Page<DisplayComponentDto> dtoPage = page.map(DisplayComponentDto::from);
        return ResponseEntity.ok(dtoPage);
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
    public ResponseEntity<Component> createComponent(
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
        List<Component> components = componentService.findAllByVehicle(vehicle);
        return components.stream().map(DisplayComponentDto::from).toList();
    }

    @GetMapping("/findByVehicleAndCondition/{vehicleId}/{condition}")
    public List<DisplayComponentDto> findAllByVehicleAndCondition(@PathVariable Long vehicleId, @PathVariable String condition) {
        Condition con = Condition.valueOf(condition);
        Vehicle vehicle = vehicleService.findById(vehicleId);
        List<Component> components = componentService.findAllByVehicleAndCondition(vehicle,con);
        return components.stream().map(DisplayComponentDto::from).toList();
    }
}
