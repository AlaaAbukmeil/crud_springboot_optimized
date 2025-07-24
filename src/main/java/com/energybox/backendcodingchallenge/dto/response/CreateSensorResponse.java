package com.energybox.backendcodingchallenge.dto.response;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class CreateSensorResponse {
    private Long id;
    private String name;
    private Instant createdAt;
    private SensorResponse.GatewayInfo gateway;
    private List<String> types;
    private String message;


    public CreateSensorResponse(SensorResponse sensor) {
        this.id = sensor.getId();
        this.name = sensor.getName();
        this.createdAt = sensor.getCreatedAt();
        this.gateway = sensor.getGateway();
        this.types = sensor.getTypes();
        this.message = "New sensor with id: " + sensor.getId() + " was created.";
    }
}