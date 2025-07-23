package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.domain.SensorType;
import com.energybox.backendcodingchallenge.repository.GatewayRepository;
import com.energybox.backendcodingchallenge.repository.SensorRepository;
import com.energybox.backendcodingchallenge.repository.SensorTypeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SensorService {
    private final SensorRepository sensorRepo;
    private final GatewayRepository gatewayRepo;
    private final SensorTypeRepository typeRepo;


    public List<Sensor> findAll() {
        return sensorRepo.findAll();
    }

    public List<Sensor> findByGateway(Long gatewayId) {
        return sensorRepo.findByGatewayId(gatewayId);
    }

    public List<Sensor> findByType(String type) {
        return sensorRepo.findByTypes_Type(type);
    }

    public Sensor create(Sensor sensor) {
        // resolve sensor types from DB
        Set<SensorType> resolved = new HashSet<>();
        for (SensorType t : sensor.getTypes()) {
            SensorType st = typeRepo.findByType(t.getType())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Unknown type: " + t.getType()
                    ));
            resolved.add(st);
        }
        sensor.setTypes(resolved);
        return sensorRepo.save(sensor);
    }

    public Sensor assignGateway(Long sensorId, Long gatewayId) {
        Sensor s = sensorRepo.findById(sensorId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sensor not found"
                ));

        Gateway g = gatewayRepo.findById(gatewayId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Gateway not found"
                ));

        s.setGateway(g);
        return sensorRepo.save(s);
    }

    public Sensor addSensorType(Long sensorId, String typeName) {
        Sensor sensor = findSensorById(sensorId);
        SensorType sensorType = findSensorTypeByName(typeName);

        sensor.getTypes().add(sensorType);
        return sensorRepo.save(sensor);
    }

    public Sensor removeSensorType(Long sensorId, String typeName) {
        Sensor sensor = findSensorById(sensorId);
        SensorType sensorType = findSensorTypeByName(typeName);

        sensor.getTypes().remove(sensorType);
        return sensorRepo.save(sensor);
    }

    public void deleteSensor(Long id) {
        sensorRepo.deleteById(id);
    }

    //helper functions
    private SensorType findSensorTypeByName(String typeName) {
        return typeRepo.findByType(typeName)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Unknown sensor type: " + typeName
                ));
    }

    private Set<SensorType> resolveAndValidateTypes(Set<SensorType> types) {
        return types.stream()
                .map(type -> findSensorTypeByName(type.getType()))
                .collect(Collectors.toSet());
    }

    private Sensor findSensorById(Long id) {
        return sensorRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sensor with id " + id + " not found"
                ));
    }

}
