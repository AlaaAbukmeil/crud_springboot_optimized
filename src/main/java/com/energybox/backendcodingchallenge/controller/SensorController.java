package com.energybox.backendcodingchallenge.controller;

import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.domain.SensorLastReading;
import com.energybox.backendcodingchallenge.dto.SensorMapper;
import com.energybox.backendcodingchallenge.dto.request.CreateSensorRequest;
import com.energybox.backendcodingchallenge.dto.request.SensorLastReadingRequest;
import com.energybox.backendcodingchallenge.dto.response.SensorLastReadingResponse;
import com.energybox.backendcodingchallenge.dto.response.SensorResponse;
import com.energybox.backendcodingchallenge.service.SensorLastReadingService;
import com.energybox.backendcodingchallenge.service.SensorService;
import com.energybox.backendcodingchallenge.service.SensorTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sensors")
@RequiredArgsConstructor
public class SensorController {
    private final SensorService sensorService;
    private final SensorMapper sensorMapper;
    private final SensorTypeService sensorTypeService;
    private final SensorLastReadingService sensorLastReadingService;

    @Operation(summary = "Get all sensors")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all sensors")
    @GetMapping
    public ResponseEntity<List<SensorResponse>> getAllSensors() {
        List<Sensor> sensors = sensorService.findAll();
        List<SensorResponse> response = sensorMapper.toResponse(sensors);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all sensors assigned to a certain gateway")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sensors for gateway"),
            @ApiResponse(responseCode = "404", description = "Gateway not found")
    })
    @GetMapping("/gateway/{gatewayId}")
    public ResponseEntity<List<SensorResponse>> getByGateway(@PathVariable Long gatewayId) {
        List<Sensor> sensors = sensorService.findByGateway(gatewayId);
        List<SensorResponse> response = sensorMapper.toResponse(sensors);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create a new sensor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sensor created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Gateway or sensor type not found")
    })
    @PostMapping
    public ResponseEntity<SensorResponse> createSensor(@Valid @RequestBody CreateSensorRequest request) {
        Sensor newSensor = sensorService.create(request);
        SensorResponse response = sensorMapper.toResponse(newSensor);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Assign a sensor to a gateway")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sensor assigned to gateway successfully"),
            @ApiResponse(responseCode = "404", description = "Sensor or gateway not found")
    })
    @PutMapping("/{sensorId}/gateway/{gatewayId}")
    public ResponseEntity<Void> assignToGateway(
            @PathVariable Long sensorId,
            @PathVariable Long gatewayId
    ) {
        sensorService.assignGateway(sensorId, gatewayId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all sensors with a certain type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sensors by type"),
            @ApiResponse(responseCode = "404", description = "Sensor type not found")
    })
    @GetMapping("/type/{sensorType}")
    public ResponseEntity<List<SensorResponse>> getByType(@PathVariable String sensorType) {
        List<Sensor> sensors = sensorService.findByType(sensorType);
        List<SensorResponse> response = sensorMapper.toResponse(sensors);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete sensor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sensor deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Sensor not found")
    })
    @DeleteMapping("/{sensorId}")
    public ResponseEntity<Void> deleteSensor(@PathVariable Long sensorId) {  // ← Fixed: consistent return type
        sensorService.deleteSensor(sensorId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all unassigned sensors (no gateway)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved unassigned sensors")
    @GetMapping("/unassigned")
    public ResponseEntity<List<SensorResponse>> getUnassigned() {
        List<Sensor> sensors = sensorService.findUnassigned();
        List<SensorResponse> response = sensorMapper.toResponse(sensors);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Add a sensor type to a sensor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sensor type added successfully"),
            @ApiResponse(responseCode = "404", description = "Sensor or sensor type not found"),
    })
    @PostMapping("/{sensorId}/types/{typeName}")
    public ResponseEntity<SensorResponse> addType(
            @PathVariable Long sensorId,
            @PathVariable String typeName
    ) {
        Sensor updated = sensorService.addSensorType(sensorId, typeName);
        SensorResponse response = sensorMapper.toResponse(updated);
        return ResponseEntity.ok(response);
    }



    @Operation(summary = "Remove a sensor type from a sensor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sensor type removed successfully"),
            @ApiResponse(responseCode = "404", description = "Sensor or sensor type not found"),
    })
    @DeleteMapping("/{sensorId}/types/{typeName}")
    public ResponseEntity<Void> removeType(
                                             @PathVariable Long sensorId,
                                             @PathVariable String typeName
    ) {
        sensorService.removeSensorType(sensorId, typeName);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add a new sensor type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sensor created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
    })
    @PostMapping("/types/{typeName}")
    public ResponseEntity<String> createSensorType(@PathVariable String typeName) {
        sensorTypeService.create(typeName);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created");
    }

    @Operation(summary = "Remove a sensor type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sensor type removed successfully"),
            @ApiResponse(responseCode = "404", description = "Sensor type not found"),
    })
    @DeleteMapping("/types/{typeName}")
    public ResponseEntity<Void> removeSensorType(
            @PathVariable String typeName
    ) {
        sensorTypeService.delete(typeName);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Populate with random data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Populated with random data"),
            @ApiResponse(responseCode = "404", description = "Unexpected error")
    })
    @PostMapping("/random")
    public ResponseEntity<String> populateWithRandomData() {
        sensorService.generateGatewaysAndSensors();
        return ResponseEntity.ok("Populated");
    }

    @Operation(summary = "Get last readings of a sensor for each type)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful."),
            @ApiResponse(responseCode = "404", description = "Sensor not found"),
    })
    @GetMapping("/readings/{sensorId}")
    public ResponseEntity<List<SensorLastReadingResponse>> getLastReadings(@PathVariable Long sensorId) {
        List<SensorLastReading> readings = sensorLastReadingService.findBySensorId(sensorId);
        List<SensorLastReadingResponse> result = sensorMapper.toResponseList(readings);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Add a new reading")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added a new reading"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Sensor or sensor type not found")
    })
    @PostMapping
    public ResponseEntity<SensorLastReadingResponse> createSensor(@Valid @RequestBody SensorLastReadingRequest request) {
        SensorLastReading sensorLastReading = sensorLastReadingService.createOrUpdateLastReading(request);
        SensorLastReadingResponse res = sensorMapper.toResponse(sensorLastReading);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}