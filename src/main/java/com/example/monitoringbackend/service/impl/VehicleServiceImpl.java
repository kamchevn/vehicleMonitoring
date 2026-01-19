package com.example.monitoringbackend.service.impl;

import com.example.monitoringbackend.exceptions.InsertDateTooLateException;
import com.example.monitoringbackend.exceptions.IntervalDoesNotMatchException;
import com.example.monitoringbackend.exceptions.VehicleNotFoundException;
import com.example.monitoringbackend.model.*;
import com.example.monitoringbackend.repository.ComponentCheckRepository;
import com.example.monitoringbackend.repository.IntervalInsertRepository;
import com.example.monitoringbackend.repository.VehicleRepository;
import com.example.monitoringbackend.service.ComponentService;
import com.example.monitoringbackend.service.ComponentTemplateService;
import com.example.monitoringbackend.service.VehicleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.monitoringbackend.service.specifications.FieldFilterSpecification.*;

@Service
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final ComponentService componentService;
    private final ComponentTemplateService componentTemplateService;
    private final ComponentCheckRepository componentCheckRepository;
    private final IntervalInsertRepository intervalInsertRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, ComponentService componentService, ComponentTemplateService componentTemplateService, ComponentCheckRepository componentCheckRepository, IntervalInsertRepository intervalInsertRepository) {
        this.vehicleRepository = vehicleRepository;
        this.componentService = componentService;
        this.componentTemplateService = componentTemplateService;
        this.componentCheckRepository = componentCheckRepository;
        this.intervalInsertRepository = intervalInsertRepository;
    }

    @Override
    public Vehicle findById(Long id) {
        return vehicleRepository.findById(id).orElseThrow(() -> new VehicleNotFoundException(id));
    }

    @Override
    public Page<Vehicle> findPage(String name, String type, Integer pageNum, Integer pageSize) {
        Specification<Vehicle> spec = Specification.allOf();

        if (name != null && !name.isEmpty()) {
            spec = spec.and(filterContainsText(Vehicle.class, "name", name));
        }

        if (type != null && !type.isEmpty()) {
            spec = spec.and(filterEqualsV(Vehicle.class, "type", type));
        }

        return this.vehicleRepository.findAll(
                spec,
                PageRequest.of(pageNum, pageSize)
        );
    }

    @Override
    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    @Override
    public Vehicle createNewVehicle(String name, int year, int totalKilometers, String type, String fuelType, String coolingType, String drivenType, String condition, String insertPeriodType) {
        Condition cdn = Condition.valueOf(condition);
        VehicleType vt = VehicleType.valueOf(type);
        VehicleFuelType ft = null;
        if(fuelType != null){
            ft = VehicleFuelType.valueOf(fuelType);
        }
        CoolingType ct = null;
        if(coolingType != null){
            ct = CoolingType.valueOf(coolingType);
        }
        DrivenType dt = null;
        if(drivenType != null){
            dt = DrivenType.valueOf(drivenType);
        }
        IntervalInsertPeriod insertPeriod = IntervalInsertPeriod.valueOf(insertPeriodType);
        return vehicleRepository.save(new Vehicle(name,year,totalKilometers,vt,ft,ct,dt,cdn,insertPeriod));
    }

    @Override
    public Vehicle editVehicle(Long id, String name, int year, int totalKilometers, String type, String fuelType, String coolingType, String drivenType, String condition, String insertPeriodType) {
        Vehicle vehicle = this.findById(id);
        Condition cdn = Condition.valueOf(condition);
        VehicleFuelType oldFuelType = vehicle.getFuelType();
        CoolingType oldCoolingType = vehicle.getCoolingType();
        DrivenType oldDrivenType = vehicle.getDrivenType();
        VehicleType vt = VehicleType.valueOf(type);
        VehicleFuelType ft = null;
        if(fuelType != null){
            ft = VehicleFuelType.valueOf(fuelType);
        }
        CoolingType ct = null;
        if(coolingType != null){
            ct = CoolingType.valueOf(coolingType);
        }
        DrivenType dt = null;
        if(drivenType != null){
            dt = DrivenType.valueOf(drivenType);
        }
        IntervalInsertPeriod insertPeriod = IntervalInsertPeriod.valueOf(insertPeriodType);
        vehicle.setName(name);
        vehicle.setYear(year);
        vehicle.setTotalKilometers(totalKilometers);
        vehicle.setType(vt);
        vehicle.setFuelType(ft);
        vehicle.setCoolingType(ct);
        vehicle.setDrivenType(dt);
        vehicle.setCondition(cdn);
        vehicle.setInsertPeriodType(insertPeriod);

        if (oldFuelType != null && ft != null && !oldFuelType.equals(ft)) {
            List<Component> components = componentService.findAllByVehicle(vehicle);

            if (oldFuelType == VehicleFuelType.PETROL && ft == VehicleFuelType.DIESEL) {
                components.stream()
                        .filter(c -> c.getTemplate().getComponentType().equals(ComponentType.SPARK_PLUG))
                        .forEach(component -> componentService.deleteComponent(component.getId()));

                ComponentTemplate glowPlugTemplate = componentTemplateService.getTemplatesForVehicleTypeAndComponentType(VehicleType.CAR, ComponentType.GLOW_PLUG).get(0);

                Component newComponent = new Component(vehicle, glowPlugTemplate, Condition.UNKNOWN, 0);
                componentService.save(newComponent);

            } else if (oldFuelType == VehicleFuelType.DIESEL && ft == VehicleFuelType.PETROL) {
                components.stream()
                        .filter(c -> c.getTemplate().getComponentType().equals(ComponentType.GLOW_PLUG))
                        .forEach(component -> componentService.deleteComponent(component.getId()));

                ComponentTemplate sparkPlugTemplate = componentTemplateService.getTemplatesForVehicleTypeAndComponentType(VehicleType.CAR, ComponentType.SPARK_PLUG).get(0);

                Component newComponent = new Component(vehicle, sparkPlugTemplate, Condition.UNKNOWN, 0);
                componentService.save(newComponent);
            }
        }
        if (vt == VehicleType.MOTORCYCLE){
            List<Component> newMotorcycleComponents = new ArrayList<>();
            List<Component> currentMotorcycleComponents = componentService.findAllByVehicle(vehicle);
            if (oldCoolingType != null && ct != null && !oldCoolingType.equals(ct)) {
                if (oldCoolingType == CoolingType.AIR_COOLED && ct == CoolingType.WATER_COOLED){
                    ComponentTemplate coolantTemplate = componentTemplateService.getTemplatesForVehicleTypeAndComponentType(VehicleType.MOTORCYCLE, ComponentType.COOLANT).get(0);
                    newMotorcycleComponents.add(new Component(vehicle, coolantTemplate, Condition.UNKNOWN, 0));
                }
                else if (oldCoolingType == CoolingType.WATER_COOLED && ct == CoolingType.AIR_COOLED){
                    currentMotorcycleComponents.stream()
                            .filter(c -> c.getTemplate().getComponentType().equals(ComponentType.COOLANT))
                            .forEach(component -> componentService.deleteComponent(component.getId()));
                }
            }
            if (oldDrivenType != null && dt != null && !oldDrivenType.equals(dt)){
                if (oldDrivenType == DrivenType.BELT_DRIVEN && dt == DrivenType.CHAIN_DRIVEN){
                    currentMotorcycleComponents.stream()
                            .filter(c -> c.getTemplate().getComponentType().equals(ComponentType.DRIVE_BELT))
                            .forEach(component -> componentService.deleteComponent(component.getId()));

                    ComponentTemplate driveChainTemplate = componentTemplateService.getTemplatesForVehicleTypeAndComponentType(VehicleType.MOTORCYCLE, ComponentType.DRIVE_CHAIN).get(0);
                    ComponentTemplate chainLubeTemplate = componentTemplateService.getTemplatesForVehicleTypeAndComponentType(VehicleType.MOTORCYCLE, ComponentType.CHAIN_LUBRICATION).get(0);
                    ComponentTemplate sprocketsTemplate = componentTemplateService.getTemplatesForVehicleTypeAndComponentType(VehicleType.MOTORCYCLE, ComponentType.SPROCKETS).get(0);

                    newMotorcycleComponents.add(new Component(vehicle, driveChainTemplate, Condition.UNKNOWN, 0));
                    newMotorcycleComponents.add(new Component(vehicle, chainLubeTemplate, Condition.UNKNOWN, 0));
                    newMotorcycleComponents.add(new Component(vehicle, sprocketsTemplate, Condition.UNKNOWN, 0));
                }
                else if (oldDrivenType == DrivenType.CHAIN_DRIVEN && dt == DrivenType.BELT_DRIVEN){
                    currentMotorcycleComponents.stream()
                            .filter(c -> c.getTemplate().getComponentType().equals(ComponentType.DRIVE_CHAIN) || c.getTemplate().getComponentType().equals(ComponentType.CHAIN_LUBRICATION) || c.getTemplate().getComponentType().equals(ComponentType.SPROCKETS))
                            .forEach(component -> componentService.deleteComponent(component.getId()));

                    ComponentTemplate driveBeltTemplate = componentTemplateService.getTemplatesForVehicleTypeAndComponentType(VehicleType.MOTORCYCLE, ComponentType.DRIVE_BELT).get(0);

                    newMotorcycleComponents.add(new Component(vehicle, driveBeltTemplate, Condition.UNKNOWN, 0));
                }
            }

            componentService.saveAll(newMotorcycleComponents);
        }

        changeVehicleCondition(id);

        return vehicle;
    }

    @Override
    public Vehicle deleteVehicle(Long id) {
        Vehicle vehicle = this.findById(id);
        List<ComponentCheck> checksForVehicle = componentCheckRepository.findAllByVehicle(vehicle);
        for(ComponentCheck check : checksForVehicle){
            check.getComponentDetails().clear();
            componentCheckRepository.delete(check);
        }
        List<Component> components = componentService.findAllByVehicle(vehicle);
        for(Component component : components){
            componentService.deleteComponent(component.getId());
        }

        List<IntervalInsert> intervalInserts = intervalInsertRepository.findAllByVehicle(vehicle);
        intervalInsertRepository.deleteAll(intervalInserts);

        vehicleRepository.delete(vehicle);
        return vehicle;
    }

    @Override
    public void insertDistanceOrFuelForVehicle(Long vehicleId, String unitType, Integer amount, LocalDateTime insertTime) throws IntervalDoesNotMatchException, InsertDateTooLateException{
        Vehicle vehicle = this.findById(vehicleId);
        IntervalUnit unit = IntervalUnit.valueOf(unitType);
        List<Component> components = componentService.findAllByVehicleAndTemplateMeasuringUnitType(vehicle,unitType);

        for(Component component : components){
            component.setCounter(component.getCounter() + amount);
        }

        componentService.saveAll(components);

        long totalComponents = components.size();

        long needsCheckCount = components.stream()
                .filter(Component::isNeedsCheck)
                .count();

        vehicle.setNeedsCheck(
                totalComponents > 0 &&
                        ((double) needsCheckCount / totalComponents) >= 0.3
        );

        if(unit == IntervalUnit.KILOMETERS){
            vehicle.setLastInsertKilometers(LocalDateTime.now());
            vehicle.setTotalKilometers(vehicle.getTotalKilometers() + amount);
        }
        else if(unit == IntervalUnit.BURNT_FUEL){
            vehicle.setLastInsertFuel(LocalDateTime.now());
        }

        vehicleRepository.save(vehicle);
        intervalInsertRepository.save(new IntervalInsert(LocalDateTime.now(),vehicle,unit,amount));
        changeVehicleCondition(vehicleId);
    }

    @Override
    public void changeVehicleCondition(Long id) {
        Vehicle vehicle = this.findById(id);
        List<Component> components = componentService.findAllByVehicle(vehicle);

        for (Component component : components) {
            if (component.getCondition() == Condition.UNKNOWN) {
                continue;
            }

            ComponentTemplate template = component.getTemplate();
            int counter = component.getCounter();
            int warn = template.getWarningInterval();
            int min = template.getMinCheckInterval();

            Condition con;
            if (counter < warn) {
                con = Condition.VERY_GOOD;
            } else if (counter < min) {
                con = Condition.GOOD;
            } else if (counter < min * 1.3) {
                con = Condition.POOR;
            } else {
                con = Condition.OOS;
            }

            component.setCondition(con);
            component.setWarningFlag(con == Condition.GOOD);
            component.setNeedsCheck(con == Condition.POOR || con == Condition.OOS);
        }

        componentService.saveAll(components);

        Map<Condition, Long> count =
                components.stream()
                        .collect(Collectors.groupingBy(
                                Component::getCondition,
                                Collectors.counting()
                        ));

        long unknown = count.getOrDefault(Condition.UNKNOWN, 0L);

        if (unknown > 0) {
            vehicle.setCondition(Condition.UNKNOWN);
            vehicleRepository.save(vehicle);
            return;
        }

        int total = components.size();

        long vg = count.getOrDefault(Condition.VERY_GOOD, 0L);
        long g  = count.getOrDefault(Condition.GOOD, 0L);
        long p  = count.getOrDefault(Condition.POOR, 0L);
        long o  = count.getOrDefault(Condition.OOS, 0L);

        Condition con = Condition.VERY_GOOD;

        if ((double) o / total >= 0.2) {
            con = Condition.OOS;
        } else if ((double) (p + o) / total >= 0.3) {
            con = Condition.POOR;
        } else if ((double) (g + vg) / total >= 0.7) {
            con = Condition.GOOD;
        }

        vehicle.setCondition(con);
        if (components.stream().anyMatch(Component::isWarningFlag)) {
            vehicle.setHasWarnings(true);
        }

        long totalComponents = components.size();

        long needsCheckCount = components.stream()
                .filter(Component::isNeedsCheck)
                .count();

        vehicle.setNeedsCheck(
                totalComponents > 0 &&
                        ((double) needsCheckCount / totalComponents) >= 0.3
        );

        vehicleRepository.save(vehicle);
    }
}
