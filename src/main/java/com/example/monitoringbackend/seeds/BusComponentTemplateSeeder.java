package com.example.monitoringbackend.seeds;

import com.example.monitoringbackend.model.*;
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
                new ComponentTemplate("Engine Oil", VehicleType.BUS, ComponentType.ENGINE_OIL, IntervalUnit.KILOMETERS, 20000, 3000),
                new ComponentTemplate("Oil Filter", VehicleType.BUS, ComponentType.OIL_FILTER, IntervalUnit.KILOMETERS, 20000, 3000),
                new ComponentTemplate("Air Filter", VehicleType.BUS, ComponentType.AIR_FILTER, IntervalUnit.KILOMETERS, 40000, 5000),
                new ComponentTemplate("Fuel Filter", VehicleType.BUS, ComponentType.FUEL_FILTER, IntervalUnit.KILOMETERS, 50000, 7000),
                new ComponentTemplate("Glow Plugs", VehicleType.BUS, ComponentType.GLOW_PLUG, IntervalUnit.KILOMETERS, 80000, 10000),
                new ComponentTemplate("Engine Belt", VehicleType.BUS, ComponentType.ENGINE_BELT, IntervalUnit.KILOMETERS, 60000, 10000),
                new ComponentTemplate("Crankcase Breaker Filter", VehicleType.BUS, ComponentType.CRANKCASE_BREAKER_FILTER, IntervalUnit.KILOMETERS, 80000, 12000),
                new ComponentTemplate("SCR", VehicleType.BUS, ComponentType.SCR, IntervalUnit.KILOMETERS, 200_000, 150_000),
                new ComponentTemplate("DPF Filter", VehicleType.BUS, ComponentType.DPF_FILTER, IntervalUnit.KILOMETERS, 180_000, 150_000),

                // Cooling
                new ComponentTemplate("Coolant", VehicleType.BUS, ComponentType.COOLANT, IntervalUnit.BURNT_FUEL, 2500, 2000),
                new ComponentTemplate("Water Pump", VehicleType.BUS, ComponentType.WATER_PUMP, IntervalUnit.KILOMETERS, 150_000, 120_000),
                new ComponentTemplate("Thermostat", VehicleType.BUS, ComponentType.THERMOSTAT, IntervalUnit.KILOMETERS, 120_000, 100_000),

                // Brakes
                new ComponentTemplate("Brake Pads", VehicleType.BUS, ComponentType.BRAKE_PADS, IntervalUnit.KILOMETERS, 50_000, 40_000),
                new ComponentTemplate("Brake Discs", VehicleType.BUS, ComponentType.BRAKE_DISCS, IntervalUnit.KILOMETERS, 100_000, 80_000),
                new ComponentTemplate("Brake Drums", VehicleType.BUS, ComponentType.BRAKE_DRUMS, IntervalUnit.KILOMETERS, 100_000, 80_000),
                new ComponentTemplate("Brake Fluid", VehicleType.BUS, ComponentType.BRAKE_FLUID, IntervalUnit.KILOMETERS, 50_000, 40_000),

                // Transmission & Drivetrain
                new ComponentTemplate("Transmission Oil", VehicleType.BUS, ComponentType.TRANSMISSION_OIL, IntervalUnit.KILOMETERS, 80_000, 60_000),
                new ComponentTemplate("Transmission Filter", VehicleType.BUS, ComponentType.TRANSMISSION_FILTER, IntervalUnit.KILOMETERS, 80_000, 60_000),
                new ComponentTemplate("Driveshaft", VehicleType.BUS, ComponentType.DRIVESHAFT, IntervalUnit.KILOMETERS, 120_000, 100_000),

                // HVAC
                new ComponentTemplate("HVAC Filter", VehicleType.BUS, ComponentType.HVAC_FILTER, IntervalUnit.KILOMETERS, 20_000, 15_000),
                new ComponentTemplate("Auxiliary Filter", VehicleType.BUS, ComponentType.AUXILIARY_FILTER, IntervalUnit.KILOMETERS, 30_000, 20_000)
        );

        templates.forEach(template -> {
            if (!repository.existsByNameAndVehicleType(template.getName(),VehicleType.BUS)) {
                repository.save(template);
            }
        });
    }
}
