package com.example.monitoringbackend.service.impl;

import com.example.monitoringbackend.exceptions.InsertDateTooLateException;
import com.example.monitoringbackend.exceptions.IntervalDoesNotMatchException;
import com.example.monitoringbackend.exceptions.VehicleNotFoundException;
import com.example.monitoringbackend.model.*;
import com.example.monitoringbackend.repository.ConditionCheckRepository;
import com.example.monitoringbackend.repository.IntervalInsertRepository;
import com.example.monitoringbackend.repository.VehicleRepository;
import com.example.monitoringbackend.service.VehicleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.monitoringbackend.service.specifications.FieldFilterSpecification.*;

@Service
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final IntervalInsertRepository intervalInsertRepository;
    private final ConditionCheckRepository conditionCheckRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, IntervalInsertRepository intervalInsertRepository, ConditionCheckRepository conditionCheckRepository) {
        this.vehicleRepository = vehicleRepository;
        this.intervalInsertRepository = intervalInsertRepository;
        this.conditionCheckRepository = conditionCheckRepository;
    }

    @Override
    public Vehicle getVehicleByInternalCode(String internalCode) {
        return vehicleRepository.findById(internalCode).orElseThrow(() -> new VehicleNotFoundException(internalCode));
    }

    @Override
    public Page<Vehicle> findPage(String department, String type, Integer pageNum, Integer pageSize) {
        Specification<Vehicle> spec = Specification.allOf();

        if (department != null && !department.isEmpty()) {
            spec = spec.and(filterEqualsV(Vehicle.class, "department", department));
        }

        if (type != null && !type.isEmpty()) {
            spec = spec.and(filterEquals(Vehicle.class, "type", type));
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
    public List<Vehicle> findAllByDepartment(String department) {
        Department dpt = Department.valueOf(department.toUpperCase());
        return vehicleRepository.findAllByDepartment(dpt);
    }

    @Override
    public List<Vehicle> findAllByType(String type) {
        return vehicleRepository.findAllByTypeIgnoreCase(type);
    }

    @Override
    public List<Vehicle> findAllByDepartmentAndType(String department, String type) {
        Department dpt = Department.valueOf(department.toUpperCase());
        return vehicleRepository.findAllByDepartmentAndType(dpt,type);
    }

    @Override
    public Vehicle createNewVehicle(String internalCode, String name, int year, String department, String type, String condition, String minCheckInterval, String maxCheckInterval, String counter) {
        Department dpt = Department.valueOf(department);
        Condition cdn = Condition.valueOf(condition);
        return vehicleRepository.save(new Vehicle(internalCode,name,year,dpt,type,cdn,minCheckInterval,maxCheckInterval,counter,null,null));
    }

    @Override
    public Vehicle editVehicle(String internalCode, String name, int year, String department, String type, String condition, String minCheckInterval, String maxCheckInterval, String counter) {
        Vehicle vehicle = this.getVehicleByInternalCode(internalCode);
        Department dpt = Department.valueOf(department);
        Condition cdn = Condition.valueOf(condition);
        vehicle.setName(name);
        vehicle.setYear(year);
        vehicle.setDepartment(dpt);
        vehicle.setType(type);
        vehicle.setCondition(cdn);
        vehicle.setMinCheckInterval(minCheckInterval);
        vehicle.setMaxCheckInterval(maxCheckInterval);
        if(!counter.isEmpty()){
            vehicle.setCounter(counter);
            String infoType = counter.substring(counter.length() - 2);
            Integer counterNum = Integer.parseInt(vehicle.getCounter().substring(0, vehicle.getCounter().length() - 2));
            Integer minIntervalNum = Integer.parseInt(vehicle.getMinCheckInterval().substring(0, vehicle.getMinCheckInterval().length() - 2));
            if(infoType.endsWith("ts")){
                counterNum = Integer.parseInt(vehicle.getCounter().substring(0, vehicle.getCounter().length() - 6));
                minIntervalNum = Integer.parseInt(vehicle.getMinCheckInterval().substring(0, vehicle.getMinCheckInterval().length() - 6));
            }

            vehicle.setNeedsCheck(counterNum >= minIntervalNum);
        }
        else{
            vehicle.setCounter(null);
        }
        return vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle deleteVehicle(String internalCode) {
        Vehicle vehicle = this.getVehicleByInternalCode(internalCode);
        vehicleRepository.delete(vehicle);
        return vehicle;
    }

    @Override
    public void insertDistanceOrHoursForVehicle(String internalCode, String information, LocalDateTime insertTime) throws IntervalDoesNotMatchException, InsertDateTooLateException{
        Vehicle vehicle = this.getVehicleByInternalCode(internalCode);

        //BITNO: Treba da se vnesuvaat podatocite izmegju 1-10 den od tekovniot mesec
        boolean dateDiffRule = insertTime.getDayOfMonth() <= 15;
        if(!dateDiffRule){
            throw new InsertDateTooLateException();
        }

        String unitType = vehicle.getCounter().substring(vehicle.getCounter().length() - 2);
        if(unitType.equals("ts")){
            unitType = vehicle.getCounter().substring(vehicle.getCounter().length() - 6);
        }
        String infoType = information.substring(information.length() - 2);
        if(infoType.equals("ts")){
            infoType = information.substring(information.length() - 6);
        }
        if(!infoType.equals(unitType)){
            throw new IntervalDoesNotMatchException();
        }
        Integer counterNum = Integer.parseInt(vehicle.getCounter().substring(0, vehicle.getCounter().length() - 2));
        Integer infoNum = Integer.parseInt(information.substring(0, information.length() - 2));
        if(unitType.equals("starts") && infoType.equals("starts")){
            counterNum = Integer.parseInt(vehicle.getCounter().substring(0, vehicle.getCounter().length() - 6));
            infoNum = Integer.parseInt(information.substring(0, information.length() - 6));
        }

        vehicle.setCounter(String.format("%d%s", counterNum + infoNum, unitType));
        vehicle.setLastInsertTime(insertTime);
        vehicleRepository.save(vehicle);
        intervalInsertRepository.save(new IntervalInsert(LocalDateTime.now(),vehicle,information));
        changeVehicleCondition(internalCode);
    }

    @Override
    public void changeVehicleCondition(String internalCode) {
        Vehicle vehicle = this.getVehicleByInternalCode(internalCode);

        String counter = vehicle.getCounter();
        String min = vehicle.getMinCheckInterval();
        String max = vehicle.getMaxCheckInterval();

        String unit = counter.endsWith("ts")
                ? counter.substring(counter.length() - 6)
                : counter.substring(counter.length() - 2);

        Integer counterNum = Integer.parseInt(counter.replace(unit, ""));
        Integer minInterval = Integer.parseInt(min.replace(unit, ""));

        Integer maxInterval = null;
        boolean isYearly = false;

        if (max != null && !max.isEmpty()) {
            if (max.equalsIgnoreCase("Yearly")) {
                isYearly = true;
            } else {
                maxInterval = Integer.parseInt(max.replace(unit, ""));
            }
        }

        boolean shouldChange = false;

        if (counterNum >= minInterval) {
            shouldChange = true;
        }

        if (maxInterval != null && counterNum >= maxInterval) {
            shouldChange = true;
        }

        if (isYearly) {
            LocalDateTime nextYearCheck = vehicle.getLastChecked().plusYears(1);
            if (!LocalDateTime.now().isBefore(nextYearCheck)) {
                shouldChange = true;
            }
        }

        if (shouldChange) {
            vehicle.setCondition(vehicle.getCondition().next());
            vehicle.setNeedsCheck(true);
            vehicleRepository.save(vehicle);
        }
    }

    @Override
    public void checkConditionForVehicle(String internalCode, String checkType, String previousCondition, String currentCondition, String note, LocalDateTime checkTime) {
        Vehicle vehicle = this.getVehicleByInternalCode(internalCode);
        vehicle.setCondition(Condition.valueOf(currentCondition));
        vehicle.setLastChecked(checkTime);
        vehicle.setNeedsCheck(false);
        vehicle.setCounter(null);
        vehicleRepository.save(vehicle);
        conditionCheckRepository.save(new ConditionCheck(note,checkTime,vehicle,Condition.valueOf(previousCondition),Condition.valueOf(currentCondition), ConditionCheckType.valueOf(checkType)));
    }
}
