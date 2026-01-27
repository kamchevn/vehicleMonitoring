package com.example.monitoringbackend.web;

import com.example.monitoringbackend.exceptions.IntervalDoesNotMatchException;
import com.example.monitoringbackend.exceptions.VehicleNotFoundException;
import com.example.monitoringbackend.model.dto.DisplayVehicleDto;
import com.example.monitoringbackend.service.application.VehicleApplicationService;
import com.example.monitoringbackend.service.domain.VehicleService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/vehicle")
public class VehicleController {
    private final VehicleApplicationService vehicleService;
    private final VehicleService domainService;

    public VehicleController(VehicleApplicationService vehicleService, VehicleService domainService) {
        this.vehicleService = vehicleService;
        this.domainService = domainService;
    }

    @GetMapping
    public List<DisplayVehicleDto> getVehicles(){
        return vehicleService.findAll();
    }

    @GetMapping("/page")
    public ResponseEntity<Page<DisplayVehicleDto>> getProductPage(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "1") String pageNum,
            @RequestParam(defaultValue = "4") String pageSize
    ) {
        Page<DisplayVehicleDto> page = this.vehicleService.findPage(user, name, type, Integer.parseInt(pageNum)-1, Integer.parseInt(pageSize));
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
    public ResponseEntity<DisplayVehicleDto> createVehicle(
                                 @RequestParam String name,
                                 @RequestParam String year,
                                 @RequestParam String totalKilometers,
                                 @RequestParam String type,
                                 @RequestParam(required = false) String fuelType,
                                 @RequestParam(required = false) String coolingType,
                                 @RequestParam(required = false) String drivenType,
                                 @RequestParam String condition,
                                 @RequestParam String insertPeriodType,
                                 @AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok(vehicleService.createNewVehicle(name,Integer.parseInt(year),Integer.parseInt(totalKilometers),type,fuelType,coolingType,drivenType,condition,insertPeriodType,user));
    }
    @PostMapping("/edit/{id}")
    public ResponseEntity<?> editVehicle(@PathVariable Long id,
                               @RequestParam String name,
                               @RequestParam String year,
                               @RequestParam String totalKilometers,
                               @RequestParam String type,
                               @RequestParam(required = false) String fuelType,
                               @RequestParam(required = false) String coolingType,
                               @RequestParam(required = false) String drivenType,
                               @RequestParam String condition,
                               @RequestParam String insertPeriodType,
                               @AuthenticationPrincipal UserDetails user){
        try {
            return ResponseEntity.ok(vehicleService.editVehicle(id,name,Integer.parseInt(year),Integer.parseInt(totalKilometers),type,fuelType,coolingType,drivenType,condition,insertPeriodType,user));
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

    @PostMapping("/insertInterval/{vehicleId}")
    public ResponseEntity<String> insertKilometersOrHoursForVehicle(@PathVariable Long vehicleId, @RequestParam String unitType, @RequestParam String amount,
                                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime insertTime,
                                                                    @RequestParam boolean late){
        try {
            Integer toAdd = Integer.parseInt(amount);
            domainService.insertDistanceOrFuelForVehicle(vehicleId, unitType, toAdd, insertTime, late);
            return ResponseEntity.ok(String.format("Successfully inserted %d%s for vehicle %d.", toAdd, unitType.equals("KILOMETERS") ? "km" : "L", vehicleId));
        }
        catch (IntervalDoesNotMatchException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
