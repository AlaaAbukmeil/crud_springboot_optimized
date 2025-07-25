package com.energybox.backendcodingchallenge.controller;

import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.domain.SensorLastReading;
import com.energybox.backendcodingchallenge.dto.SensorMapper;
import com.energybox.backendcodingchallenge.dto.request.CreateSensorRequest;
import com.energybox.backendcodingchallenge.dto.request.SensorLastReadingRequest;
import com.energybox.backendcodingchallenge.dto.response.SensorLastReadingResponse;
import com.energybox.backendcodingchallenge.dto.response.SensorResponse;
import com.energybox.backendcodingchallenge.dto.response.SensorResponseWithSuggestion;
import com.energybox.backendcodingchallenge.service.SensorLastReadingService;
import com.energybox.backendcodingchallenge.service.SensorService;
import com.energybox.backendcodingchallenge.service.SensorTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Page<SensorResponse>> getAllSensors(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field", example = "name")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir) {


        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Sensor> sensors = sensorService.findAll(pageable);
        Page<SensorResponse> response = sensors.map(sensorMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get sensor by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sensor"),
            @ApiResponse(responseCode = "404", description = "sensor not found")
    })
    @GetMapping("/{sensorId}")
    public ResponseEntity<SensorResponse> getBySensorId(@PathVariable Long sensorId) {
        Sensor sensor = sensorService.findById(sensorId);

        SensorResponse response = sensorMapper.toResponse(sensor);
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
    @PutMapping("/{sensorId}/assign")
    public ResponseEntity<Void> assignToGateway(
            @PathVariable Long sensorId,
            @Parameter(description = "Gateway Id", example = "1")
            @RequestParam(defaultValue = "1") Long gatewayId
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

    @Operation(summary = "Get all unassigned sensors with suggested closest gateways")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved unassigned sensors with gateway suggestions")
    @GetMapping("/unassigned")
    public ResponseEntity<List<SensorResponseWithSuggestion>> getUnassigned() {
        List<SensorResponseWithSuggestion> sensorsWithSuggestions =
                sensorService.findUnassignedWithSuggestedGateways();
        return ResponseEntity.ok(sensorsWithSuggestions);
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

    @Operation(summary = "Get last readings of a sensor for each type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful."),
            @ApiResponse(responseCode = "404", description = "Sensor not found"),
    })
    @GetMapping("/readings/{sensorId}")
    public ResponseEntity<List<SensorLastReadingResponse>> getLastReadingsForSensors(@PathVariable Long sensorId) {
        List<SensorLastReading> readings = sensorLastReadingService.findBySensorId(sensorId);
        List<SensorLastReadingResponse> result = sensorMapper.toResponseList(readings);
        return ResponseEntity.ok(result);
    }
    @Operation(summary = "Get last readings for all sensors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful."),
            @ApiResponse(responseCode = "404", description = "Sensor not found"),
    })
    @GetMapping("/readings")
    public ResponseEntity<List<SensorLastReadingResponse>> getLastReadings() {
        List<SensorLastReading> readings = sensorLastReadingService.findAll();
        List<SensorLastReadingResponse> result = sensorMapper.toResponseList(readings);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Add a new reading")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added a new reading"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Sensor or sensor type not found")
    })
    @PostMapping("/readings/sensor/{sensorId}")
    public ResponseEntity<SensorLastReadingResponse> addAReading(@Valid @RequestBody SensorLastReadingRequest sensorLastReadingRequest) {
        SensorLastReading sensorLastReading = sensorLastReadingService.create(sensorLastReadingRequest);
        SensorLastReadingResponse res = sensorMapper.toResponse(sensorLastReading);



        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @Operation(summary = "Get all sensor types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful."),
            @ApiResponse(responseCode = "404", description = "Sensor not found"),
    })
    @GetMapping("/types")
    public ResponseEntity<List<String>> getAllSensorTypes() {
        List<String> readings = sensorTypeService.getAllSensorTypeNames();

        return ResponseEntity.ok(readings);
    }

}