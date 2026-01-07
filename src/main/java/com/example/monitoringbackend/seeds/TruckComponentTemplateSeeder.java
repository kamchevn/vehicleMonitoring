package com.example.monitoringbackend.seeds;

import com.example.monitoringbackend.model.*;
import com.example.monitoringbackend.repository.ComponentTemplateRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TruckComponentTemplateSeeder {

    private final ComponentTemplateRepository repository;

    public TruckComponentTemplateSeeder(ComponentTemplateRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void seed() {
        seedTruckComponents();
    }

    private void seedTruckComponents() {
        List<ComponentTemplate> templates = List.of(
                // Engine & Fluids
                new ComponentTemplate("Engine Oil", VehicleType.TRUCK, ComponentType.ENGINE_OIL, IntervalUnit.KILOMETERS, 25000, 5000),
                new ComponentTemplate("Oil Filter", VehicleType.TRUCK, ComponentType.OIL_FILTER, IntervalUnit.KILOMETERS, 25000, 5000),
                new ComponentTemplate("Air Filter", VehicleType.TRUCK, ComponentType.AIR_FILTER, IntervalUnit.KILOMETERS, 50000, 8000),
                new ComponentTemplate("Fuel Filter", VehicleType.TRUCK, ComponentType.FUEL_FILTER, IntervalUnit.KILOMETERS, 60000, 10000),
                new ComponentTemplate("Glow Plugs", VehicleType.TRUCK, ComponentType.GLOW_PLUG, IntervalUnit.KILOMETERS, 100_000, 15_000),
                new ComponentTemplate("Engine Belt", VehicleType.TRUCK, ComponentType.ENGINE_BELT, IntervalUnit.KILOMETERS, 80_000, 10_000),
                new ComponentTemplate("Timing Gear", VehicleType.TRUCK, ComponentType.TIMING_GEAR, IntervalUnit.KILOMETERS, 120_000, 20_000),
                new ComponentTemplate("SCR", VehicleType.TRUCK, ComponentType.SCR, IntervalUnit.KILOMETERS, 250_000, 200_000),
                new ComponentTemplate("DPF Filter", VehicleType.TRUCK, ComponentType.DPF_FILTER, IntervalUnit.KILOMETERS, 200_000, 150_000),

                // Transmission & Drivetrain
                new ComponentTemplate("Transmission Oil", VehicleType.TRUCK, ComponentType.TRANSMISSION_OIL, IntervalUnit.KILOMETERS, 120_000, 100_000),
                new ComponentTemplate("Transmission Filter", VehicleType.TRUCK, ComponentType.TRANSMISSION_FILTER, IntervalUnit.KILOMETERS, 120_000, 100_000),
                new ComponentTemplate("Driveshaft", VehicleType.TRUCK, ComponentType.DRIVESHAFT, IntervalUnit.KILOMETERS, 150_000, 120_000),

                // Braking
                new ComponentTemplate("Brake Pads", VehicleType.TRUCK, ComponentType.BRAKE_PADS, IntervalUnit.KILOMETERS, 60_000, 50_000),
                new ComponentTemplate("Brake Discs", VehicleType.TRUCK, ComponentType.BRAKE_DISCS, IntervalUnit.KILOMETERS, 120_000, 100_000),
                new ComponentTemplate("Brake Fluid", VehicleType.TRUCK, ComponentType.BRAKE_FLUID, IntervalUnit.KILOMETERS, 60_000, 50_000),

                // HVAC
                new ComponentTemplate("Auxiliary Filter", VehicleType.TRUCK, ComponentType.AUXILIARY_FILTER, IntervalUnit.KILOMETERS, 30_000, 25_000)
        );

        templates.forEach(template -> {
            if (!repository.existsByNameAndVehicleType(template.getName(),VehicleType.TRUCK)) {
                repository.save(template);
            }
        });
    }
}
