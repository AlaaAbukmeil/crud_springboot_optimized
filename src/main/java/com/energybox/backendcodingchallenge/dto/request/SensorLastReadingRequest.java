package com.energybox.backendcodingchallenge.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorLastReadingRequest {

    @NotNull(message = "Sensor ID is required")
    private Long sensorId;

    @NotNull(message = "Sensor type is required")
    private String sensorType;

    @NotNull(message = "Reading value is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Reading value must be greater than 0")
    private BigDecimal readingValue;

    private Instant readingTime;
}