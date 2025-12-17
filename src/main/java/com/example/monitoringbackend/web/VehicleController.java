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
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "1") String pageNum,
            @RequestParam(defaultValue = "4") String pageSize
    ) {
        Page<Vehicle> page = this.vehicleService.findPage(department, type, Integer.parseInt(pageNum)-1, Integer.parseInt(pageSize));
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{internalCode}")
    public ResponseEntity<?> getVehicleByInternalCode(@PathVariable String internalCode){
        try {
            return ResponseEntity.ok(vehicleService.getVehicleByInternalCode(internalCode));
        } catch (VehicleNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Vehicle> createVehicle(@RequestParam String internalCode,
                                 @RequestParam String name,
                                 @RequestParam String year,
                                 @RequestParam String department,
                                 @RequestParam String type,
                                 @RequestParam String condition,
                                 @RequestParam String minCheckInterval,
                                 @RequestParam String maxCheckInterval,
                                 @RequestParam String counter){
        return ResponseEntity.ok(vehicleService.createNewVehicle(internalCode,name,Integer.parseInt(year),department,type,condition,minCheckInterval,maxCheckInterval,counter));
    }
    @PostMapping("/edit/{internalCode}")
    public ResponseEntity<?> editVehicle(@PathVariable String internalCode,
                               @RequestParam String name,
                               @RequestParam String year,
                               @RequestParam String department,
                               @RequestParam String type,
                               @RequestParam String condition,
                               @RequestParam String minCheckInterval,
                               @RequestParam String maxCheckInterval,
                               @RequestParam String counter){
        try {
            return ResponseEntity.ok(vehicleService.editVehicle(internalCode,name,Integer.parseInt(year),department,type,condition,minCheckInterval,maxCheckInterval,counter));
        } catch (VehicleNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/delete/{internalCode}")
    public ResponseEntity<?> deleteVehicle(@PathVariable String internalCode){
        try {
            return ResponseEntity.ok(vehicleService.deleteVehicle(internalCode));
        } catch (VehicleNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/insertInterval/{internalCode}")
    public ResponseEntity<String> insertKilometersOrHoursForVehicle(@PathVariable String internalCode, @RequestParam String information,
                                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime insertTime){
        try {
            vehicleService.insertDistanceOrHoursForVehicle(internalCode, information, insertTime);
            return ResponseEntity.ok(String.format("Successfully inserted %s for vehicle %s.", information, internalCode));
        }
        catch (IntervalDoesNotMatchException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
