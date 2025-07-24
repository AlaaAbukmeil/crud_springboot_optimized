package com.energybox.backendcodingchallenge.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateSensorRequest {

    @NotBlank(message = "Sensor name is required")
    @Size(max = 100, message = "Sensor name must not exceed 100 characters")
    private String name;

    @NotEmpty(message = "At least one sensor type is required")
    private List<String> types;

    @Valid
    private GatewayReference gateway;

    @Data
    public static class GatewayReference {
        @NotNull(message = "Gateway ID is required")
        private Long id;
    }
}
