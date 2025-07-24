package com.energybox.backendcodingchallenge.dto.response;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class GatewayResponse {
    private Long id;
    private String name;
    private Instant createdAt;
    private List<SensorInfo> sensors;

    @Data
    public static class SensorInfo {
        private Long id;
        private String name;
        private List<String> types;
    }
}