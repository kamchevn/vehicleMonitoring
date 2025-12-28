package com.example.monitoringbackend.service.impl;

import com.example.monitoringbackend.exceptions.InsertDateTooLateException;
import com.example.monitoringbackend.exceptions.IntervalDoesNotMatchException;
import com.example.monitoringbackend.exceptions.VehicleNotFoundException;
import com.example.monitoringbackend.model.*;
import com.example.monitoringbackend.repository.IntervalInsertRepository;
import com.example.monitoringbackend.repository.VehicleRepository;
import com.example.monitoringbackend.service.ComponentService;
import com.example.monitoringbackend.service.VehicleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.monitoringbackend.service.specifications.FieldFilterSpecification.*;

@Service
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final ComponentService componentService;
    private final IntervalInsertRepository intervalInsertRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, ComponentService componentService, IntervalInsertRepository intervalInsertRepository) {
        this.vehicleRepository = vehicleRepository;
        this.componentService = componentService;
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
    public Vehicle createNewVehicle(String name, int year, String department, String type, String condition) {
        Department dpt = Department.valueOf(department);
        Condition cdn = Condition.valueOf(condition);
        VehicleType vt = VehicleType.valueOf(type);
        return vehicleRepository.save(new Vehicle(name,year,dpt,vt,cdn));
    }

    @Override
    public Vehicle editVehicle(Long id, String name, int year, String department, String type, String condition) {
        Vehicle vehicle = this.findById(id);
        Department dpt = Department.valueOf(department);
        Condition cdn = Condition.valueOf(condition);
        VehicleType vt = VehicleType.valueOf(type);
        vehicle.setName(name);
        vehicle.setYear(year);
        vehicle.setDepartment(dpt);
        vehicle.setType(vt);
        vehicle.setCondition(cdn);
        return vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle deleteVehicle(Long id) {
        Vehicle vehicle = this.findById(id);
        vehicleRepository.delete(vehicle);
        return vehicle;
    }

    @Override
    public void insertDistanceOrHoursForVehicle(Long id, String information, LocalDateTime insertTime) throws IntervalDoesNotMatchException, InsertDateTooLateException{
        Vehicle vehicle = this.findById(id);
        List<Component> components;
        Integer numberToAdd;

        //BITNO: Treba da se vnesuvaat podatocite izmegju 1-10 den od tekovniot mesec
        boolean dateDiffRule = insertTime.getDayOfMonth() <= 10;
        if(!dateDiffRule){
            throw new InsertDateTooLateException();
        }

        String infoType = information.substring(information.length() - 2);
        if(infoType.equals("km")){
            components = componentService.findAllByVehicleAndTemplateMeasuringUnitType(vehicle,"KILOMETERS");
            numberToAdd = Integer.parseInt(information.substring(0,information.length()-2));
        }
        else {
            components = componentService.findAllByVehicleAndTemplateMeasuringUnitType(vehicle,"BURNT_FUEL");
            numberToAdd = Integer.parseInt(information.substring(0,information.length()-1));
        }

        for(Component component : components){
            component.setCounter(component.getCounter()+numberToAdd);
        }

        componentService.saveAll(components);

        long total = components.size();
        long needingCheck = components.stream()
                .filter(Component::isNeedsCheck)
                .count();
        vehicle.setNeedsCheck(((double) needingCheck / total) >= 0.3);

        vehicleRepository.save(vehicle);
        intervalInsertRepository.save(new IntervalInsert(LocalDateTime.now(),vehicle,information));
        changeVehicleCondition(id);
    }

    @Override
    public void changeVehicleCondition(Long id) {
        Vehicle vehicle = this.findById(id);
        List<Component> components = componentService.findAllByVehicle(vehicle);

        for(Component component : components){
            ComponentTemplate template = component.getTemplate();
            int counter = component.getCounter();
            int warn = template.getWarningInterval();
            int min = template.getMinCheckInterval();
            Condition con;
            if (counter < warn) {
                con = Condition.VERY_GOOD;
            }
            else if (counter < min) {
                con = Condition.GOOD;
            }
            else if (counter < min * 1.3) {
                con = Condition.POOR;
            }
            else {
                con = Condition.OOS;
            }
            component.setCondition(con);
            component.setNeedsCheck(con == Condition.POOR || con == Condition.OOS);
        }
        componentService.saveAll(components);

        int total = components.size();

        Map<Condition, Long> count =
                components.stream()
                        .collect(Collectors.groupingBy(
                                Component::getCondition,
                                Collectors.counting()
                        ));

        long vg = count.getOrDefault(Condition.VERY_GOOD, 0L);
        long g  = count.getOrDefault(Condition.GOOD, 0L);
        long p  = count.getOrDefault(Condition.POOR, 0L);
        long o  = count.getOrDefault(Condition.OOS, 0L);

        //default, pa potoa pravi proverki i gi mesti spored components
        Condition con = Condition.VERY_GOOD;
        vehicle.setCondition(Condition.VERY_GOOD);

        if ((double) o / total >= 0.2) {
            con = Condition.OOS;
        }
        else if ((double) (p + o) / total >= 0.3) {
            con = Condition.POOR;
        }
        else if ((double) (g + vg) / total >= 0.7) {
            con = Condition.GOOD;
        }

        vehicle.setCondition(con);
        vehicleRepository.save(vehicle);
    }
}
