package com.example.monitoringbackend.seeds;

import com.example.monitoringbackend.model.*;
import com.example.monitoringbackend.model.enumerations.ComponentType;
import com.example.monitoringbackend.model.enumerations.IntervalUnit;
import com.example.monitoringbackend.model.enumerations.VehicleType;
import com.example.monitoringbackend.repository.ComponentTemplateRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import org.springframework.stereotype.Component;

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
    List<ComponentTemplate> templates =
        List.of(
            // 🔧 Engine & Fluids
            new ComponentTemplate(
                "Engine Oil",
                VehicleType.CAR,
                ComponentType.ENGINE_OIL,
                IntervalUnit.BURNT_FUEL,
                1200,
                1000), // ili 10000km ili 1 godina koristenje
            new ComponentTemplate(
                "Oil Filter",
                VehicleType.CAR,
                ComponentType.OIL_FILTER,
                IntervalUnit.BURNT_FUEL,
                1200,
                1000), // se menuva so masloto vo komplet
            new ComponentTemplate(
                "Air Filter",
                VehicleType.CAR,
                ComponentType.AIR_FILTER,
                IntervalUnit.KILOMETERS,
                10000,
                8000),
            new ComponentTemplate(
                "Fuel Filter",
                VehicleType.CAR,
                ComponentType.FUEL_FILTER,
                IntervalUnit.KILOMETERS,
                10000,
                8000),
            new ComponentTemplate(
                "Spark Plugs",
                VehicleType.CAR,
                ComponentType.SPARK_PLUG,
                IntervalUnit.KILOMETERS,
                50000,
                40000),
            new ComponentTemplate(
                "Glow Plugs",
                VehicleType.CAR,
                ComponentType.GLOW_PLUG,
                IntervalUnit.KILOMETERS,
                50000,
                40000),

            // 🧪 Emissions
            new ComponentTemplate(
                "Gas Sensor",
                VehicleType.CAR,
                ComponentType.OXYGEN_SENSOR,
                IntervalUnit.KILOMETERS,
                100000,
                80000), // sonda za gasovi

            // ❄ Cooling
            new ComponentTemplate(
                "Engine Coolant",
                VehicleType.CAR,
                ComponentType.COOLANT,
                IntervalUnit.BURNT_FUEL,
                1200,
                1000),
            new ComponentTemplate(
                "Water Pump",
                VehicleType.CAR,
                ComponentType.WATER_PUMP,
                IntervalUnit.KILOMETERS,
                50000,
                40000),
            new ComponentTemplate(
                "Thermostat",
                VehicleType.CAR,
                ComponentType.THERMOSTAT,
                IntervalUnit.KILOMETERS,
                40000,
                40000),

            // 🔋 Electrical
            new ComponentTemplate(
                "Battery",
                VehicleType.CAR,
                ComponentType.BATTERY,
                IntervalUnit.KILOMETERS,
                60000,
                50000),

            // 🛑 Braking
            new ComponentTemplate(
                "Brake Pads",
                VehicleType.CAR,
                ComponentType.BRAKE_PADS,
                IntervalUnit.KILOMETERS,
                50000,
                40000),
            new ComponentTemplate(
                "Brake Discs",
                VehicleType.CAR,
                ComponentType.BRAKE_DISCS,
                IntervalUnit.KILOMETERS,
                100000,
                80000),
            new ComponentTemplate(
                "Brake Fluid",
                VehicleType.CAR,
                ComponentType.BRAKE_FLUID,
                IntervalUnit.KILOMETERS,
                50000,
                40000),
            new ComponentTemplate(
                "Tires",
                VehicleType.CAR,
                ComponentType.TIRES,
                IntervalUnit.KILOMETERS,
                50000,
                40000), // na 5-6 meseci koristenje
            new ComponentTemplate(
                "Pollen Filter",
                VehicleType.CAR,
                ComponentType.CABIN_AIR_FILTER,
                IntervalUnit.KILOMETERS,
                10000,
                8000));

    // Save only if not already present
    for (ComponentTemplate template : templates) {
      if (!repository.existsByNameAndVehicleType(template.getName(), VehicleType.CAR)) {
        repository.save(template);
      }
    }
  }
}
