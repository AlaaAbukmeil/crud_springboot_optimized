package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.dto.GatewayMapper;
import com.energybox.backendcodingchallenge.dto.request.CreateGatewayRequest;
import com.energybox.backendcodingchallenge.repository.GatewayRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GatewayService {
    private final GatewayRepository repo;
    private final SensorTypeService sensorTypeService;
    private final GatewayMapper gatewayMapper;

    public List<Gateway> findAll() {
        return repo.findAll();
    }

    public Gateway create(CreateGatewayRequest request) {
        Gateway gateway = gatewayMapper.fromCreateRequest(request);
        return repo.save(gateway);
    }

    public List<Gateway> findWithSensorType(String type) {
        sensorTypeService.findSensorTypeByName(type);
        return repo.findDistinctBySensors_Types_Type(type);
    }

    public void deleteById(Long id) {
        findGatewayById(id);
        repo.deleteById(id);
    }
    public Gateway findGatewayById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gateway with id " + id + " not found"));

    }
}
