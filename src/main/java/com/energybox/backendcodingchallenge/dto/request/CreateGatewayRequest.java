package com.energybox.backendcodingchallenge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateGatewayRequest {

    @NotBlank(message = "Gateway name is required")
    @Size(max = 100, message = "Gateway name must not exceed 100 characters")
    private String name;
}