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
    List<ComponentTemplate> templates =
        List.of(
            // Engine & Fluids
            new ComponentTemplate(
                "Engine Oil",
                VehicleType.TRUCK,
                ComponentType.ENGINE_OIL,
                IntervalUnit.BURNT_FUEL,
                14000,
                11000),
            new ComponentTemplate(
                "Oil Filter",
                VehicleType.TRUCK,
                ComponentType.OIL_FILTER,
                IntervalUnit.BURNT_FUEL,
                14000,
                11000),
            new ComponentTemplate(
                "Air Filter",
                VehicleType.TRUCK,
                ComponentType.AIR_FILTER,
                IntervalUnit.KILOMETERS,
                50000,
                30000),
            new ComponentTemplate(
                "Fuel Filter",
                VehicleType.TRUCK,
                ComponentType.FUEL_FILTER,
                IntervalUnit.KILOMETERS,
                60000,
                45000),
            new ComponentTemplate(
                "Engine Belt",
                VehicleType.TRUCK,
                ComponentType.ENGINE_BELT,
                IntervalUnit.KILOMETERS,
                80000,
                60000),

            // Transmission & Drivetrain
            new ComponentTemplate(
                "Clutch",
                VehicleType.TRUCK,
                ComponentType.CLUTCH,
                IntervalUnit.KILOMETERS,
                120000,
                100000),
            new ComponentTemplate(
                "Axle Oil",
                VehicleType.TRUCK,
                ComponentType.AXLE_OIL,
                IntervalUnit.KILOMETERS,
                120000,
                100000),

            // Cooling
            new ComponentTemplate(
                "Coolant",
                VehicleType.TRUCK,
                ComponentType.COOLANT,
                IntervalUnit.BURNT_FUEL,
                24000,
                19000),
            new ComponentTemplate(
                "Water Pump",
                VehicleType.TRUCK,
                ComponentType.WATER_PUMP,
                IntervalUnit.KILOMETERS,
                150000,
                120000),
            new ComponentTemplate(
                "Thermostat",
                VehicleType.TRUCK,
                ComponentType.THERMOSTAT,
                IntervalUnit.KILOMETERS,
                120000,
                100000),

            // Braking
            new ComponentTemplate(
                "Brake Pads",
                VehicleType.TRUCK,
                ComponentType.BRAKE_PADS,
                IntervalUnit.KILOMETERS,
                60000,
                50000),
            new ComponentTemplate(
                "Brake Discs",
                VehicleType.TRUCK,
                ComponentType.BRAKE_DISCS,
                IntervalUnit.KILOMETERS,
                120000,
                100000),
            new ComponentTemplate(
                "Brake Drums",
                VehicleType.TRUCK,
                ComponentType.BRAKE_DRUMS,
                IntervalUnit.KILOMETERS,
                120000,
                100000),
            new ComponentTemplate(
                "Brake Fluid",
                VehicleType.TRUCK,
                ComponentType.BRAKE_FLUID,
                IntervalUnit.KILOMETERS,
                100000,
                80000),
            new ComponentTemplate(
                "Tires",
                VehicleType.TRUCK,
                ComponentType.TIRES,
                IntervalUnit.KILOMETERS,
                90000,
                70000),

            // HVAC
            new ComponentTemplate(
                "Auxiliary Filter",
                VehicleType.TRUCK,
                ComponentType.AUXILIARY_FILTER,
                IntervalUnit.KILOMETERS,
                40000,
                30000),
            new ComponentTemplate(
                "Cabin Air Filter",
                VehicleType.TRUCK,
                ComponentType.CABIN_AIR_FILTER,
                IntervalUnit.KILOMETERS,
                40000,
                30000));

    templates.forEach(
        template -> {
          if (!repository.existsByNameAndVehicleType(template.getName(), VehicleType.TRUCK)) {
            repository.save(template);
          }
        });
  }
}
