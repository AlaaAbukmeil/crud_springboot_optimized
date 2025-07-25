package com.energybox.backendcodingchallenge.dto;

import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.domain.SensorLastReading;
import com.energybox.backendcodingchallenge.domain.SensorType;
import com.energybox.backendcodingchallenge.dto.request.CreateSensorRequest;
import com.energybox.backendcodingchallenge.dto.response.SensorLastReadingResponse;
import com.energybox.backendcodingchallenge.dto.response.SensorResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SensorMapper {

    public SensorResponse toResponse(Sensor sensor) {
        if (sensor == null) {
            return null;
        }

        SensorResponse response = new SensorResponse();
        response.setId(sensor.getId());
        response.setName(sensor.getName());
        response.setCreatedAt(sensor.getCreatedAt());

        // Map gateway
        if (sensor.getGateway() != null) {
            SensorResponse.GatewayInfo gatewayInfo = new SensorResponse.GatewayInfo();
            gatewayInfo.setId(sensor.getGateway().getId());
            gatewayInfo.setName(sensor.getGateway().getName());
            response.setGateway(gatewayInfo);
        }

        // Map sensor types to string list
        if (sensor.getTypes() != null) {
            List<String> types = sensor.getTypes().stream()
                    .map(SensorType::getType)
                    .collect(Collectors.toList());
            response.setTypes(types);
        }

        return response;
    }


    public List<SensorResponse> toResponse(List<Sensor> sensors) {
        if (sensors == null) {
            return null;
        }
        return sensors.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<SensorResponse> toListResponse(List<Sensor> sensors) {
        if (sensors == null) {
            return null;
        }
        return sensors.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Sensor fromCreateRequest(CreateSensorRequest request) {
        if (request == null) {
            return null;
        }

        Sensor sensor = new Sensor();
        sensor.setName(request.getName());


        return sensor;
    }

    public SensorLastReadingResponse toResponse(SensorLastReading entity) {
        if (entity == null) {
            return null;
        }

        return new SensorLastReadingResponse(
                entity.getSensor().getName(),
                entity.getSensorType().getType(),
                entity.getReadingTime(),
                entity.getReadingValue()
        );
    }


    public List<SensorLastReadingResponse> toResponseList(List<SensorLastReading> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }


    public SensorLastReading toEntity(SensorLastReadingResponse dto) {
        if (dto == null) {
            return null;
        }

        SensorLastReading entity = new SensorLastReading();
        entity.setReadingTime(dto.getReadingTime());
        entity.setReadingValue(dto.getReadingValue());

        return entity;
    }
}