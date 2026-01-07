package com.example.monitoringbackend.web;

import com.example.monitoringbackend.model.*;
import com.example.monitoringbackend.service.ComponentTemplateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/template")
public class ComponentTemplateController {
    private final ComponentTemplateService componentTemplateService;

    public ComponentTemplateController(ComponentTemplateService componentTemplateService) {
        this.componentTemplateService = componentTemplateService;
    }

    @GetMapping("/{vehicleType}")
    private List<ComponentTemplate> getTemplatesForVehicleType(@PathVariable String vehicleType,
                                                               @RequestParam(required = false) String fuelType,
                                                               @RequestParam(required = false) String coolingType){
        VehicleType vt = VehicleType.valueOf(vehicleType);
        List<ComponentTemplate> componentTemplates = componentTemplateService.getTemplatesForVehicleType(vt);
        if(fuelType != null && VehicleFuelType.valueOf(fuelType) == VehicleFuelType.PETROL) {
            componentTemplates = componentTemplates.stream()
                    .filter(t -> t.getComponentType() != ComponentType.GLOW_PLUG)
                    .toList();
        } else if(fuelType != null && VehicleFuelType.valueOf(fuelType) == VehicleFuelType.DIESEL) {
            componentTemplates = componentTemplates.stream()
                    .filter(t -> t.getComponentType() != ComponentType.SPARK_PLUG)
                    .toList();
        }
        if(coolingType != null && CoolingType.valueOf(coolingType) == CoolingType.AIR_COOLED) {
            componentTemplates = componentTemplates.stream()
                    .filter(t -> t.getComponentType() != ComponentType.COOLANT && t.getComponentType() != ComponentType.WATER_PUMP)
                    .toList();
        }
        return componentTemplates;
    }

    @GetMapping("/{vehicleType}/{componentType}")
    private List<ComponentTemplate> getTemplatesForVehicleTypeAndComponentType(@PathVariable String vehicleType, @PathVariable String componentType){
        VehicleType vt = VehicleType.valueOf(vehicleType);
        ComponentType ct = ComponentType.valueOf(componentType);
        return componentTemplateService.getTemplatesForVehicleTypeAndComponentType(vt,ct);
    }
}
