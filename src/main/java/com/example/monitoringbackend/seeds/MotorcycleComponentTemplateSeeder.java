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
                // 🔧 Engine & Fluids
                new ComponentTemplate("Engine Oil", VehicleType.MOTORCYCLE, ComponentType.ENGINE_OIL, IntervalUnit.BURNT_FUEL, 300, 220),
                new ComponentTemplate("Oil Filter", VehicleType.MOTORCYCLE, ComponentType.OIL_FILTER, IntervalUnit.BURNT_FUEL, 300, 220),
                new ComponentTemplate("Air Filter", VehicleType.MOTORCYCLE, ComponentType.AIR_FILTER, IntervalUnit.KILOMETERS, 10000, 8000),
                new ComponentTemplate("Fuel Filter", VehicleType.MOTORCYCLE, ComponentType.FUEL_FILTER, IntervalUnit.KILOMETERS, 15000, 10000),
                new ComponentTemplate("Spark Plug", VehicleType.MOTORCYCLE, ComponentType.SPARK_PLUG, IntervalUnit.KILOMETERS, 10000, 8000),

                // 💧 Water-cooled only
                new ComponentTemplate("Engine Coolant", VehicleType.MOTORCYCLE, ComponentType.COOLANT, IntervalUnit.BURNT_FUEL, 1200, 1000), //water-cooled bikes

                // 🔄 Transmission & Drivetrain
                new ComponentTemplate("Clutch Cable", VehicleType.MOTORCYCLE, ComponentType.CLUTCH_CABLE, IntervalUnit.KILOMETERS, 15000, 12000),
                new ComponentTemplate("Drive Chain", VehicleType.MOTORCYCLE, ComponentType.DRIVE_CHAIN, IntervalUnit.KILOMETERS, 15000, 12000), //chain-driven
                new ComponentTemplate("Chain Lubrication", VehicleType.MOTORCYCLE, ComponentType.CHAIN_LUBRICATION, IntervalUnit.KILOMETERS, 2000, 1000), //chain-driven
                new ComponentTemplate("Drive Belt", VehicleType.MOTORCYCLE, ComponentType.DRIVE_BELT, IntervalUnit.KILOMETERS, 15000, 12000), // for belt-driven bikes
                new ComponentTemplate("Drive Oil", VehicleType.MOTORCYCLE, ComponentType.DRIVE_OIL, IntervalUnit.KILOMETERS, 10000, 8000),
                new ComponentTemplate("Sprockets", VehicleType.MOTORCYCLE, ComponentType.SPROCKETS, IntervalUnit.KILOMETERS, 30000, 25000), //chain-driven

                // ❄ Suspension & Steering
                new ComponentTemplate("Front Fork Oil", VehicleType.MOTORCYCLE, ComponentType.FRONT_FORK_OIL, IntervalUnit.KILOMETERS, 15000, 12000),
                new ComponentTemplate("Rear Shock Absorber", VehicleType.MOTORCYCLE, ComponentType.REAR_SHOCK_ABSORBER, IntervalUnit.KILOMETERS, 20000, 15000),
                new ComponentTemplate("Steering Head Bearings", VehicleType.MOTORCYCLE, ComponentType.STEERING_HEAD_BEARINGS, IntervalUnit.KILOMETERS, 30000, 20000),
                new ComponentTemplate("Swingarm Bearings", VehicleType.MOTORCYCLE, ComponentType.SWINGARM_BEARINGS, IntervalUnit.KILOMETERS, 30000, 20000),

                // 🛑 Braking
                new ComponentTemplate("Brake Pads", VehicleType.MOTORCYCLE, ComponentType.BRAKE_PADS, IntervalUnit.KILOMETERS, 10000, 8000),
                new ComponentTemplate("Brake Discs", VehicleType.MOTORCYCLE, ComponentType.BRAKE_DISCS, IntervalUnit.KILOMETERS, 30000, 25000),
                new ComponentTemplate("Brake Fluid", VehicleType.MOTORCYCLE, ComponentType.BRAKE_FLUID, IntervalUnit.KILOMETERS, 10000, 8000),

                // 🛞 Wheels & Tires
                new ComponentTemplate("Tires", VehicleType.MOTORCYCLE, ComponentType.TIRES, IntervalUnit.KILOMETERS, 12000, 10000),

                // 🧰 Controls & Wear Items
                new ComponentTemplate("Throttle Cable", VehicleType.MOTORCYCLE, ComponentType.THROTTLE_CABLE, IntervalUnit.KILOMETERS, 15000, 12000)
        );

        templates.forEach(template -> {
            if (!repository.existsByNameAndVehicleType(template.getName(),VehicleType.MOTORCYCLE)) {
                repository.save(template);
            }
        });
    }
}
