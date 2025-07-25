package com.energybox.backendcodingchallenge.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateGatewayRequest {

    @NotBlank(message = "Gateway name is required")
    @Size(max = 100, message = "Gateway name must not exceed 100 characters")
    private String name;

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


}