package com.example.monitoringbackend.seeds;

import com.example.monitoringbackend.model.*;
import com.example.monitoringbackend.repository.ComponentTemplateRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MotorcycleComponentTemplateSeeder {

    private final ComponentTemplateRepository repository;

    public MotorcycleComponentTemplateSeeder(ComponentTemplateRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void seed() {
        seedMotorcycleComponents();
    }

    private void seedMotorcycleComponents() {
        List<ComponentTemplate> templates = List.of(
                // Engine & Fluids
                new ComponentTemplate("Engine Oil", VehicleType.MOTORCYCLE, ComponentType.ENGINE_OIL, IntervalUnit.KILOMETERS, 10_000, 2000),
                new ComponentTemplate("Oil Filter", VehicleType.MOTORCYCLE, ComponentType.OIL_FILTER, IntervalUnit.KILOMETERS, 10_000, 2000),
                new ComponentTemplate("Air Filter", VehicleType.MOTORCYCLE, ComponentType.AIR_FILTER, IntervalUnit.KILOMETERS, 20_000, 4000),
                new ComponentTemplate("Spark Plugs", VehicleType.MOTORCYCLE, ComponentType.SPARK_PLUG, IntervalUnit.KILOMETERS, 20_000, 4000),
                new ComponentTemplate("Engine Coolant", VehicleType.MOTORCYCLE, ComponentType.ENGINE_COOLANT, IntervalUnit.BURNT_FUEL, 800, 200),
                new ComponentTemplate("Front Fork Oil", VehicleType.MOTORCYCLE, ComponentType.FRONT_FORK_OIL, IntervalUnit.KILOMETERS, 15_000, 2000),
                new ComponentTemplate("Rear Shock Absorber", VehicleType.MOTORCYCLE, ComponentType.REAR_SHOCK_ABSORBER, IntervalUnit.KILOMETERS, 20_000, 4000),
                new ComponentTemplate("Drive Chain", VehicleType.MOTORCYCLE, ComponentType.DRIVE_CHAIN, IntervalUnit.KILOMETERS, 20_000, 3000),
                new ComponentTemplate("Sprockets", VehicleType.MOTORCYCLE, ComponentType.SPROCKETS, IntervalUnit.KILOMETERS, 30_000, 5000),
                new ComponentTemplate("Throttle Cable", VehicleType.MOTORCYCLE, ComponentType.THROTTLE_CABLE, IntervalUnit.KILOMETERS, 20_000, 4000),
                new ComponentTemplate("Drive Oil", VehicleType.MOTORCYCLE, ComponentType.DRIVE_OIL, IntervalUnit.KILOMETERS, 20_000, 4000)
        );

        templates.forEach(template -> {
            if (!repository.existsByNameAndVehicleType(template.getName(),VehicleType.MOTORCYCLE)) {
                repository.save(template);
            }
        });
    }
}
