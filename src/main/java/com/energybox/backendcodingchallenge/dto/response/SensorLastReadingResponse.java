package com.energybox.backendcodingchallenge.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorLastReadingResponse {

    private String sensorName;
    private String sensorType;
    private Instant readingTime;
    private BigDecimal readingValue;

    public SensorLastReadingResponse(com.energybox.backendcodingchallenge.domain.SensorLastReading entity) {
        this.sensorName = entity.getSensor().getName();
        this.sensorType = entity.getSensorType().getType();
        this.readingTime = entity.getReadingTime();
        this.readingValue = entity.getReadingValue();
    }
}