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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public Page<Sensor> findAll(Pageable pageable) {
        return sensorRepo.findAll(pageable);
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




    private final Random random = new Random();

    private static final List<String> TYPE_NAMES = List.of(
            "temperature", "humidity", "electricity", "pressure", "sound"
    );



    private void generateSensorTypes() {
        TYPE_NAMES.forEach(name -> {
            sensorTypeRepository.findByType(name)
                    .orElseGet(() -> {
                        SensorType t = new SensorType();
                        t.setType(name);
                        return sensorTypeRepository.save(t);
                    });
        });
    }

    public void generateGatewaysAndSensors() {

        // 1) Get all gateways
//        List<Gateway> gateways = gatewayRepo.findAll();
        List<Gateway> gateways = IntStream.rangeClosed(1, 5)
                .mapToObj(i -> {
                    Gateway g = new Gateway();
                    g.setName("GW-" + UUID.randomUUID().toString().substring(0, 8));
                    return gatewayRepo.save(g);
                })
                .toList();
        // 2) Fetch types once
        List<SensorType> types = sensorTypeRepository.findAll();

        // 3) Create and save sensors one by one
        for (int i = 1; i <= 1000; i++) {
            Sensor s = new Sensor();
            s.setName("Sensor-" + i);

            // assign gateway 80% of the time
            if (random.nextDouble() < 0.8) {
                Gateway pick = gateways.get(random.nextInt(gateways.size()));
                s.setGateway(pick);
            }

            // Save the sensor first
            s = sensorRepo.save(s);

            // Now add types to the persisted sensor
            Collections.shuffle(types, random);
            int count = 1 + random.nextInt(3);

            for (int j = 0; j < count; j++) {
                SensorType sensorType = types.get(j);

                // Add to both sides of the relationship
                s.getTypes().add(sensorType);
                sensorType.getSensors().add(s);
            }

            // Save the sensor (owning side of the relationship)
            sensorRepo.save(s);
        }

        sensorRepo.flush();

        sensorRepo.findAll().forEach(s -> {
            String assigned = s.getTypes().stream()
                    .map(SensorType::getType)
                    .collect(Collectors.joining(", "));
            System.out.println(
                    "Sensor " + s.getName() + " has types: " + assigned
            );
        });
    }
}
