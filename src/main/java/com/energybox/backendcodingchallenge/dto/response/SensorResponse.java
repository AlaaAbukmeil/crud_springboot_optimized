package com.energybox.backendcodingchallenge.dto.response;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class SensorResponse {
    private Long id;
    private String name;
    private Instant createdAt;
//    private GatewayInfo gateway;
    private List<String> types;
    private BigDecimal xCoordinate;
    private BigDecimal yCoordinate;

//    @Data
//    public static class GatewayInfo {
//        private Long id;
//        private String name;
//        private BigDecimal xCoordinate;
//        private BigDecimal yCoordinate;
//    }
}