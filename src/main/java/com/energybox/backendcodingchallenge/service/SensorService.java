package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.domain.SensorType;
import com.energybox.backendcodingchallenge.dto.SensorMapper;
import com.energybox.backendcodingchallenge.dto.request.CreateSensorRequest;
import com.energybox.backendcodingchallenge.repository.SensorRepository;
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
    private final GatewayService gatewayService;
    private final SensorTypeService sensorTypeService;
    private final SensorMapper sensorMapper;


    public List<Sensor> findAll() {
        return sensorRepo.findAll();
    }

    public List<Sensor> findByGateway(Long gatewayId)
    {
        gatewayService.findGatewayById(gatewayId);


        return sensorRepo.findByGatewayId(gatewayId);
    }

    public List<Sensor> findByType(String type) {
        sensorTypeService.findSensorTypeByName(type);
        return sensorRepo.findByTypes_Type(type);
    }


    public Sensor create(CreateSensorRequest request) {
        // Convert DTO to entity
        Sensor sensor = sensorMapper.fromCreateRequest(request);

        // Set gateway if provided
        if (request.getGateway() != null) {
            Gateway gateway = gatewayService.findGatewayById(request.getGateway().getId());

            sensor.setGateway(gateway);
        }

        Set<SensorType> sensorTypes = request.getTypes().stream()
                .map(typeName -> sensorTypeService.findSensorTypeByName(typeName))
                .collect(Collectors.toSet());
        sensor.setTypes(sensorTypes);

        return sensorRepo.save(sensor);
    }
    public Sensor assignGateway(Long sensorId, Long gatewayId) {
        Sensor s = findSensorById(sensorId);

        Gateway g = gatewayService.findGatewayById(gatewayId);

        s.setGateway(g);
        return sensorRepo.save(s);
    }

    public Sensor addSensorType(Long sensorId, String typeName) {

        Sensor sensor = findSensorById(sensorId);
        SensorType sensorType = sensorTypeService.findSensorTypeByName(typeName);

        sensor.getTypes().add(sensorType);
        return sensorRepo.save(sensor);
    }

    public Sensor removeSensorType(Long sensorId, String typeName) {
        Sensor sensor = findSensorById(sensorId);
        SensorType sensorType = sensorTypeService.findSensorTypeByName(typeName);

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
                .map(type -> sensorTypeService.findSensorTypeByName(type.getType()))
                .collect(Collectors.toSet());
    }

    private Sensor findSensorById(Long id) {
        return sensorRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sensor with id " + id + " not found"));

    }
}
