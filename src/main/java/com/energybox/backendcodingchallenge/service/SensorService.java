package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.domain.SensorType;
import com.energybox.backendcodingchallenge.dto.request.CreateSensorRequest;
import com.energybox.backendcodingchallenge.repository.GatewayRepository;
import com.energybox.backendcodingchallenge.repository.SensorRepository;
import com.energybox.backendcodingchallenge.repository.SensorTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SensorService {
    private final SensorRepository sensorRepo;
    private final GatewayRepository gatewayRepo;
    private final SensorTypeRepository sensorTypeRepository;


    public List<Sensor> findAll() {
        return sensorRepo.findAll();
    }

    public List<Sensor> findByGateway(Long gatewayId)
    {
        gatewayRepo.findById(gatewayId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Gateway with id " + gatewayId + " not found"));


        return sensorRepo.findByGatewayId(gatewayId);
    }

    public List<Sensor> findByType(String type) {
        sensorTypeRepository.findByType(type)
                .orElseThrow(() -> new EntityNotFoundException(
                        "SensorType '" + type + "' not found"));

        return sensorRepo.findByTypes_Type(type);
    }


    public Sensor create(CreateSensorRequest request) {
        // Convert DTO to entity
        Sensor sensor = new Sensor();
        sensor.setName(request.getName());

        // Set gateway if provided
        if (request.getGateway() != null) {
            Gateway gateway = gatewayRepo.findById(request.getGateway().getId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Gateway with id " + request.getGateway().getId() + " not found"));
            sensor.setGateway(gateway);
        }

        Set<SensorType> sensorTypes = request.getTypes().stream()
                .map(typeName -> sensorTypeRepository.findByType(typeName)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "SensorType '" + typeName + "' not found")))
                .collect(Collectors.toSet());
        sensor.setTypes(sensorTypes);

        return sensorRepo.save(sensor);
    }
    public Sensor assignGateway(Long sensorId, Long gatewayId) {
        Sensor s = findSensorById(sensorId);

        Gateway g = gatewayRepo.findById(gatewayId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Gateway with id " + gatewayId + " not found"));

        s.setGateway(g);
        return sensorRepo.save(s);
    }

    public Sensor addSensorType(Long sensorId, String typeName) {

        Sensor sensor = findSensorById(sensorId);
        SensorType sensorType = sensorTypeRepository.findByType(typeName)
                .orElseThrow(() -> new EntityNotFoundException(
                        "SensorType '" + typeName + "' not found"));

        sensor.getTypes().add(sensorType);
        return sensorRepo.save(sensor);
    }

    public Sensor removeSensorType(Long sensorId, String typeName) {
        Sensor sensor = findSensorById(sensorId);
        SensorType sensorType = sensorTypeRepository.findByType(typeName)
                .orElseThrow(() -> new EntityNotFoundException(
                        "SensorType '" + typeName + "' not found"));

        sensor.getTypes().remove(sensorType);
        return sensorRepo.save(sensor);
    }

    public void deleteSensor(Long id) {
        Sensor sensor = findSensorById(id);
        sensorRepo.deleteById(id);
    }
    public List<Sensor> findUnassigned() {
        return sensorRepo.findByGatewayIsNull();
    }

    //helper functions


    private Set<SensorType> resolveAndValidateTypes(Set<SensorType> types) {
        return types.stream()
                .map(type -> sensorTypeRepository.findByType(type.getType())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "SensorType '" + type + "' not found")))
                .collect(Collectors.toSet());
    }

    private Sensor findSensorById(Long id) {
        return sensorRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sensor with id " + id + " not found"));

    }
}
