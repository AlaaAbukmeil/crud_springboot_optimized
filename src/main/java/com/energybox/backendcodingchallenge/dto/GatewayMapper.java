// mapper/GatewayMapper.java
package com.energybox.backendcodingchallenge.dto;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.domain.SensorType;
import com.energybox.backendcodingchallenge.dto.request.CreateGatewayRequest;
import com.energybox.backendcodingchallenge.dto.response.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GatewayMapper {

    public GatewayResponse toResponse(Gateway gateway) {
        if (gateway == null) {
            return null;
        }

        GatewayResponse response = new GatewayResponse();
        response.setId(gateway.getId());
        response.setName(gateway.getName());
        response.setCreatedAt(gateway.getCreatedAt());

        // Map sensors
        if (gateway.getSensors() != null) {
            List<GatewayResponse.SensorInfo> sensors = gateway.getSensors().stream()
                    .map(this::toSensorInfo)
                    .collect(Collectors.toList());
            response.setSensors(sensors);
        }

        return response;
    }



    public List<GatewayResponse> toResponse(List<Gateway> gateways) {
        if (gateways == null) {
            return null;
        }
        return gateways.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }



    public Gateway fromCreateRequest(CreateGatewayRequest request) {
        if (request == null) {
            return null;
        }

        Gateway gateway = new Gateway();
        gateway.setName(request.getName());

        return gateway;
    }


    private GatewayResponse.SensorInfo toSensorInfo(Sensor sensor) {
        GatewayResponse.SensorInfo info = new GatewayResponse.SensorInfo();
        info.setId(sensor.getId());
        info.setName(sensor.getName());

        if (sensor.getTypes() != null) {
            List<String> types = sensor.getTypes().stream()
                    .map(SensorType::getType)
                    .collect(Collectors.toList());
            info.setTypes(types);
        }

        return info;
    }




}