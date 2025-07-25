package com.energybox.backendcodingchallenge.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
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

    @NotNull(message = "X coordinate is required")
    @DecimalMin(value = "-9999.999999", message = "X coordinate must be between -9999.999999 and 9999.999999")
    @DecimalMax(value = "9999.999999", message = "X coordinate must be between -9999.999999 and 9999.999999")
    @Digits(integer = 4, fraction = 6, message = "X coordinate must have at most 4 integer digits and 6 decimal places")
    private BigDecimal xCoordinate;

    @NotNull(message = "Y coordinate is required")
    @DecimalMin(value = "-9999.999999", message = "Y coordinate must be between -9999.999999 and 9999.999999")
    @DecimalMax(value = "9999.999999", message = "Y coordinate must be between -9999.999999 and 9999.999999")
    @Digits(integer = 4, fraction = 6, message = "Y coordinate must have at most 4 integer digits and 6 decimal places")
    private BigDecimal yCoordinate;


    @Data
    public static class GatewayReference {
        @NotNull(message = "Gateway ID is required")
        private Long id;
    }
}
