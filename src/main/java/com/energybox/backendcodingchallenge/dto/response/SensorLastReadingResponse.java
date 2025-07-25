package com.energybox.backendcodingchallenge.dto.response;

import com.energybox.backendcodingchallenge.domain.SensorLastReading;
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
    private Long sensorId;

    public SensorLastReadingResponse(SensorLastReading entity) {
        this.sensorName = entity.getSensor().getName();
        this.sensorType = entity.getSensorType().getType();
        this.readingTime = entity.getReadingTime();
        this.readingValue = entity.getReadingValue();
        this.sensorId = entity.getSensor().getId();
    }
}