package com.example.monitoringbackend.web;

import com.example.monitoringbackend.exceptions.IntervalDoesNotMatchException;
import com.example.monitoringbackend.exceptions.VehicleNotFoundException;
import com.example.monitoringbackend.model.Vehicle;
import com.example.monitoringbackend.service.VehicleService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/vehicle")
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<Vehicle> getVehicles(){
        return vehicleService.findAll();
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Vehicle>> getProductPage(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "1") String pageNum,
            @RequestParam(defaultValue = "4") String pageSize
    ) {
        Page<Vehicle> page = this.vehicleService.findPage(name, type, Integer.parseInt(pageNum)-1, Integer.parseInt(pageSize));
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVehicleByInternalCode(@PathVariable Long id){
        try {
            return ResponseEntity.ok(vehicleService.findById(id));
        } catch (VehicleNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Vehicle> createVehicle(
                                 @RequestParam String name,
                                 @RequestParam String year,
                                 @RequestParam String totalKilometers,
                                 @RequestParam String type,
                                 @RequestParam(required = false) String fuelType,
                                 @RequestParam(required = false) String coolingType,
                                 @RequestParam String condition,
                                 @RequestParam String insertPeriodType){
        return ResponseEntity.ok(vehicleService.createNewVehicle(name,Integer.parseInt(year),Integer.parseInt(totalKilometers),type,fuelType,coolingType,condition,insertPeriodType));
    }
    @PostMapping("/edit/{id}")
    public ResponseEntity<?> editVehicle(@PathVariable Long id,
                               @RequestParam String name,
                               @RequestParam String year,
                               @RequestParam String totalKilometers,
                               @RequestParam String type,
                               @RequestParam(required = false) String fuelType,
                               @RequestParam(required = false) String coolingType,
                               @RequestParam String condition,
                               @RequestParam String insertPeriodType){
        try {
            return ResponseEntity.ok(vehicleService.editVehicle(id,name,Integer.parseInt(year),Integer.parseInt(totalKilometers),type,fuelType,coolingType,condition,insertPeriodType));
        } catch (VehicleNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long id){
        try {
            return ResponseEntity.ok(vehicleService.deleteVehicle(id));
        } catch (VehicleNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/insertInterval/{id}")
    public ResponseEntity<String> insertKilometersOrHoursForVehicle(@PathVariable Long id, @RequestParam String information,
                                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime insertTime){
        try {
            vehicleService.insertDistanceOrHoursForVehicle(id, information, insertTime);
            return ResponseEntity.ok(String.format("Successfully inserted %s for vehicle %d.", information, id));
        }
        catch (IntervalDoesNotMatchException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
