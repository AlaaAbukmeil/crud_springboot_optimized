package com.energybox.backendcodingchallenge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorResponseWithSuggestion {
    private Long id;
    private String name;
    private BigDecimal xCoordinate;
    private BigDecimal yCoordinate;
    private Instant createdAt;
    private List<String> types;
    private SuggestedGateway suggestedGateway;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SuggestedGateway {
        private Long id;
        private String name;
        private BigDecimal xCoordinate;
        private BigDecimal yCoordinate;
        private Double distance;
    }
}