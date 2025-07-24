package com.energybox.backendcodingchallenge.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UpdateSensorRequest {

    @Size(max = 100, message = "Sensor name must not exceed 100 characters")
    private String name;

    private List<String> types;

    private Long gatewayId;
}