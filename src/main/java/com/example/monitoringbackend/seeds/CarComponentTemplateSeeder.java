package com.example.monitoringbackend.seeds;

import com.example.monitoringbackend.model.*;
import com.example.monitoringbackend.repository.ComponentTemplateRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CarComponentTemplateSeeder {

    private final ComponentTemplateRepository repository;

    public CarComponentTemplateSeeder(ComponentTemplateRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void seed() {
        seedCarComponents();
    }

    private void seedCarComponents() {
        List<ComponentTemplate> templates = List.of(
                // 🔧 Engine & Fluids
                new ComponentTemplate(
                        "Engine Oil",
                        VehicleType.CAR,
                        ComponentType.ENGINE_OIL,
                        IntervalUnit.KILOMETERS,
                        15000,
                        2000
                ),
                new ComponentTemplate(
                        "Oil Filter",
                        VehicleType.CAR,
                        ComponentType.OIL_FILTER,
                        IntervalUnit.KILOMETERS,
                        15000,
                        2000
                ),
                new ComponentTemplate(
                        "Air Filter",
                        VehicleType.CAR,
                        ComponentType.AIR_FILTER,
                        IntervalUnit.KILOMETERS,
                        30000,
                        5000
                ),
                new ComponentTemplate(
                        "Fuel Filter",
                        VehicleType.CAR,
                        ComponentType.FUEL_FILTER,
                        IntervalUnit.KILOMETERS,
                        40000,
                        5000
                ),
                new ComponentTemplate(
                        "Spark Plugs",
                        VehicleType.CAR,
                        ComponentType.SPARK_PLUG,
                        IntervalUnit.KILOMETERS,
                        60000,
                        10000
                ),

                // 🧪 Emissions
                new ComponentTemplate(
                        "Oxygen Sensor",
                        VehicleType.CAR,
                        ComponentType.OXYGEN_SENSOR,
                        IntervalUnit.KILOMETERS,
                        120_000,
                        100_000
                ),
                new ComponentTemplate(
                        "Catalytic Converter",
                        VehicleType.CAR,
                        ComponentType.CATALYTIC_CONVERTER,
                        IntervalUnit.KILOMETERS,
                        200_000,
                        170_000
                ),
                new ComponentTemplate(
                        "EGR Valve",
                        VehicleType.CAR,
                        ComponentType.EGR_VALVE,
                        IntervalUnit.KILOMETERS,
                        150_000,
                        120_000
                ),
                new ComponentTemplate(
                        "DPF Filter",
                        VehicleType.CAR,
                        ComponentType.DPF_FILTER,
                        IntervalUnit.KILOMETERS,
                        180_000,
                        150_000
                ),

                // ❄ Cooling
                new ComponentTemplate(
                        "Engine Coolant",
                        VehicleType.CAR,
                        ComponentType.COOLANT,
                        IntervalUnit.BURNT_FUEL,
                        1200, // liters
                        1000
                ),
                new ComponentTemplate(
                        "Water Pump",
                        VehicleType.CAR,
                        ComponentType.WATER_PUMP,
                        IntervalUnit.KILOMETERS,
                        150_000,
                        120_000
                ),
                new ComponentTemplate(
                        "Thermostat",
                        VehicleType.CAR,
                        ComponentType.THERMOSTAT,
                        IntervalUnit.KILOMETERS,
                        120_000,
                        100_000
                ),

                // 🔋 Electrical
                new ComponentTemplate(
                        "Battery",
                        VehicleType.CAR,
                        ComponentType.BATTERY,
                        IntervalUnit.KILOMETERS,
                        60_000,
                        50_000
                ),
                new ComponentTemplate(
                        "Alternator",
                        VehicleType.CAR,
                        ComponentType.ALTERNATOR,
                        IntervalUnit.KILOMETERS,
                        100_000,
                        80_000
                ),

                // 🛑 Braking
                new ComponentTemplate(
                        "Brake Pads",
                        VehicleType.CAR,
                        ComponentType.BRAKE_PADS,
                        IntervalUnit.KILOMETERS,
                        40_000,
                        30_000
                ),
                new ComponentTemplate(
                        "Brake Discs",
                        VehicleType.CAR,
                        ComponentType.BRAKE_DISCS,
                        IntervalUnit.KILOMETERS,
                        80_000,
                        60_000
                ),
                new ComponentTemplate(
                        "Brake Fluid",
                        VehicleType.CAR,
                        ComponentType.BRAKE_FLUID,
                        IntervalUnit.KILOMETERS,
                        40_000,
                        30_000
                )
        );

        // Save only if not already present
        for (ComponentTemplate template : templates) {
            if (!repository.existsByNameAndVehicleType(template.getName(),VehicleType.CAR)) {
                repository.save(template);
            }
        }
    }
}
