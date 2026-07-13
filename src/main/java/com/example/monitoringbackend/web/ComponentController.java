package com.example.monitoringbackend.web;

import com.example.monitoringbackend.exceptions.VehicleNotFoundException;
import com.example.monitoringbackend.model.Component;
import com.example.monitoringbackend.model.enumerations.Condition;
import com.example.monitoringbackend.model.Vehicle;
import com.example.monitoringbackend.model.dto.DisplayComponentDto;
import com.example.monitoringbackend.service.application.ComponentApplicationService;
import com.example.monitoringbackend.service.domain.ComponentService;
import com.example.monitoringbackend.service.domain.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/component")
@Tag(name = "Component API", description = "Endpoints for components CRUD and filter searching.") // Swagger tag
public class ComponentController {
    private final ComponentApplicationService componentService;
    private final VehicleService vehicleService;

    public ComponentController(ComponentApplicationService componentService, VehicleService vehicleService) {
        this.componentService = componentService;
        this.vehicleService = vehicleService;
    }
    @Operation(summary = "List all components", description = "Finds the components and returns them as list.")
    @GetMapping
    private List<DisplayComponentDto> getComponents(){
        return componentService.findAll();
    }

    @Operation(summary = "List all components as Pageable", description = "Checks the vehicle if it exists, finds the components for that vehicle and returns them as pageable. There can be applied measuringUnit and condition filters for the components searching.")
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

    @Operation(summary = "Returns component by id", description = "Finds the component by it's id and returns the component object matching.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getComponentById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(componentService.findById(id));
        } catch (VehicleNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @Operation(summary = "Create a new component", description = "Finds the vehicle by its id, finds the component template for creation and creates the component object with the relationships.")
    @PostMapping("/create")
    public ResponseEntity<DisplayComponentDto> createComponent(
            @RequestParam Long vehicleId,
            @RequestParam Long componentTemplateId,
            @RequestParam String condition,
            @RequestParam String counter){
        Vehicle vehicle = vehicleService.findById(vehicleId);
        return ResponseEntity.ok(componentService.createNewComponent(vehicle,componentTemplateId,condition,Integer.parseInt(counter)));
    }

    @Operation(summary = "Update an existing component", description = "Finds the component by it's id and updates the content for that component in the database.")
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

    @Operation(summary = "Delete a component", description = "Deletes a component by it's id.")
    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteComponent(@PathVariable Long id){
        try {
            return ResponseEntity.ok(componentService.deleteComponent(id));
        } catch (VehicleNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @Operation(summary = "List all components by vehicle", description = "Find the vehicle by it's id and list all of the components, which have relationship with that vehicle.")
    @GetMapping("/findByVehicle/{vehicleId}")
    public List<DisplayComponentDto> findAllByVehicle(@PathVariable Long vehicleId) {
        Vehicle vehicle = vehicleService.findById(vehicleId);
        List<DisplayComponentDto> components = componentService.findAllByVehicle(vehicle);
        return components;
    }

    @Operation(summary = "List all components by vehicle and condition", description = "Find the vehicle by it's id and list all of the components, which have relationship with that vehicle and have that condition.")
    @GetMapping("/findByVehicleAndCondition/{vehicleId}/{condition}")
    public List<DisplayComponentDto> findAllByVehicleAndCondition(@PathVariable Long vehicleId, @PathVariable String condition) {
        Condition con = Condition.valueOf(condition);
        Vehicle vehicle = vehicleService.findById(vehicleId);
        List<DisplayComponentDto> components = componentService.findAllByVehicleAndCondition(vehicle,con);
        return components;
    }
}
