package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.dto.request.CreateGatewayRequest;
import com.energybox.backendcodingchallenge.repository.GatewayRepository;
import com.energybox.backendcodingchallenge.repository.SensorTypeRepository;
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
    private final SensorTypeRepository sensorTypeRepository;

    public List<Gateway> findAll() {
        return repo.findAll();
    }

    public Gateway create(CreateGatewayRequest request) {
        Gateway gateway = new Gateway();
        gateway.setName(request.getName());
        return repo.save(gateway);
    }

    public List<Gateway> findWithSensorType(String type) {
        sensorTypeRepository.findByType(type)
                .orElseThrow(() -> new EntityNotFoundException(
                        "SensorType '" + type + "' not found"));
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
