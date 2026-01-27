package com.example.monitoringbackend.seeds;

import com.example.monitoringbackend.model.*;
import com.example.monitoringbackend.model.enumerations.ComponentType;
import com.example.monitoringbackend.model.enumerations.IntervalUnit;
import com.example.monitoringbackend.model.enumerations.VehicleType;
import com.example.monitoringbackend.repository.ComponentTemplateRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BusComponentTemplateSeeder {

    private final ComponentTemplateRepository repository;

    public BusComponentTemplateSeeder(ComponentTemplateRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void seed() {
        seedBusComponents();
    }

    private void seedBusComponents() {
        List<ComponentTemplate> templates = List.of(
                // Engine & Fluids
                new ComponentTemplate("Engine Oil", VehicleType.BUS, ComponentType.ENGINE_OIL, IntervalUnit.BURNT_FUEL, 12000, 9500),
                new ComponentTemplate("Oil Filter", VehicleType.BUS, ComponentType.OIL_FILTER, IntervalUnit.BURNT_FUEL, 12000, 9500),
                new ComponentTemplate("Air Filter", VehicleType.BUS, ComponentType.AIR_FILTER, IntervalUnit.KILOMETERS, 25000, 20000),
                new ComponentTemplate("Fuel Filter", VehicleType.BUS, ComponentType.FUEL_FILTER, IntervalUnit.KILOMETERS, 25000, 20000),
                new ComponentTemplate("Engine Belt", VehicleType.BUS, ComponentType.ENGINE_BELT, IntervalUnit.KILOMETERS, 100000, 80000), // 100000km ili na sekoi 3-5 godini
                // Cooling
                new ComponentTemplate("Coolant", VehicleType.BUS, ComponentType.COOLANT, IntervalUnit.BURNT_FUEL, 20000, 16000),
                new ComponentTemplate("Water Pump", VehicleType.BUS, ComponentType.WATER_PUMP, IntervalUnit.KILOMETERS, 150000, 120000),
                new ComponentTemplate("Thermostat", VehicleType.BUS, ComponentType.THERMOSTAT, IntervalUnit.KILOMETERS, 120000, 100000),
                // Brakes
                new ComponentTemplate("Brake Pads", VehicleType.BUS, ComponentType.BRAKE_PADS, IntervalUnit.KILOMETERS, 50000, 40000),
                new ComponentTemplate("Brake Discs", VehicleType.BUS, ComponentType.BRAKE_DISCS, IntervalUnit.KILOMETERS, 100000, 80000),
                new ComponentTemplate("Brake Drums", VehicleType.BUS, ComponentType.BRAKE_DRUMS, IntervalUnit.KILOMETERS, 100000, 80000),
                new ComponentTemplate("Brake Fluid", VehicleType.BUS, ComponentType.BRAKE_FLUID, IntervalUnit.KILOMETERS, 80000, 60000),

                new ComponentTemplate("Tires", VehicleType.BUS, ComponentType.TIRES, IntervalUnit.KILOMETERS, 50000, 40000),
                // HVAC
                new ComponentTemplate("HVAC Filter", VehicleType.BUS, ComponentType.HVAC_FILTER, IntervalUnit.KILOMETERS, 20000, 15000),
                new ComponentTemplate("Auxiliary Filter", VehicleType.BUS, ComponentType.AUXILIARY_FILTER, IntervalUnit.KILOMETERS, 30000, 20000),
                new ComponentTemplate("Cabin Air Filter", VehicleType.BUS, ComponentType.CABIN_AIR_FILTER, IntervalUnit.KILOMETERS,30000,20000)
        );

        templates.forEach(template -> {
            if (!repository.existsByNameAndVehicleType(template.getName(),VehicleType.BUS)) {
                repository.save(template);
            }
        });
    }
}
