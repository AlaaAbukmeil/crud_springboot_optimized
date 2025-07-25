package com.energybox.backendcodingchallenge.controller;

import com.energybox.backendcodingchallenge.dto.GatewayMapper;
import com.energybox.backendcodingchallenge.dto.request.CreateGatewayRequest;
import com.energybox.backendcodingchallenge.dto.response.GatewayResponse;
import com.energybox.backendcodingchallenge.service.GatewayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.energybox.backendcodingchallenge.domain.Gateway;

import java.util.List;

@RestController
@RequestMapping("/gateways")
@RequiredArgsConstructor
public class GatewayController {
    private final GatewayService gatewayService;
    private final GatewayMapper  gatewayMapper;



    @Operation(summary = "Get all gateways")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all gateways")
    @GetMapping
    public ResponseEntity<List<GatewayResponse>> getAllGateways() {
        List<Gateway> gateways = gatewayService.findAll();
        List<GatewayResponse> response = gatewayMapper.toResponse(gateways);
        return ResponseEntity.ok(response);
    }



    @Operation(summary = "Create a gateway")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Gateway created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<GatewayResponse> createGateway(@Valid @RequestBody CreateGatewayRequest request) {
        Gateway newGateway = gatewayService.create(request);
        GatewayResponse gatewayResponse = gatewayMapper.toResponse(newGateway);
        return ResponseEntity.status(HttpStatus.CREATED).body(gatewayResponse);
    }





    @Operation(
            summary = "Get gateways filtered by sensor type",
            description = "Retrieves all gateways that have at least one sensor of the specified type"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved gateways with specified sensor type"),
            @ApiResponse(responseCode = "404", description = "Sensor type not found"),
            @ApiResponse(responseCode = "400", description = "Invalid sensor type parameter")
    })
    @GetMapping("with-sensors/{typeName}")
    public ResponseEntity<List<GatewayResponse>> getBySensorType(
            @PathVariable String typeName
    ) {
        List<Gateway> list = gatewayService.findWithSensorType(typeName);
        List<GatewayResponse> response = gatewayMapper.toResponse(list);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Delete gateway")
    @DeleteMapping("/{gatewayId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGateway(@PathVariable Long gatewayId) {

            gatewayService.deleteById(gatewayId);

    }


}
