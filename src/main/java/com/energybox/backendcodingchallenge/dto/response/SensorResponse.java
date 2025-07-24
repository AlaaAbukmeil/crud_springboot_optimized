package com.energybox.backendcodingchallenge.dto.response;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class SensorResponse {
    private Long id;
    private String name;
    private Instant createdAt;
    private GatewayInfo gateway;
    private List<String> types;

    @Data
    public static class GatewayInfo {
        private Long id;
        private String name;
    }
}