package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.domain.SensorType;
import com.energybox.backendcodingchallenge.dto.request.CreateSensorRequest;
import com.energybox.backendcodingchallenge.dto.response.SensorResponseWithSuggestion;
import com.energybox.backendcodingchallenge.repository.GatewayRepository;
import com.energybox.backendcodingchallenge.repository.SensorRepository;
import com.energybox.backendcodingchallenge.repository.SensorTypeRepository;
import com.energybox.backendcodingchallenge.util.DistanceUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        sensor.setXCoordinate(request.getXCoordinate());
        sensor.setYCoordinate(request.getYCoordinate());
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


    /**
     * Find unassigned sensors with suggested closest gateways
     * @return List of unassigned sensors with gateway suggestions
     */
    public List<SensorResponseWithSuggestion> findUnassignedWithSuggestedGateways() {
        List<Sensor> unassignedSensors = sensorRepo.findByGatewayIsNull();
        List<Gateway> availableGateways = gatewayRepo.findAll();

        return unassignedSensors.stream()
                .map(sensor -> createSensorWithSuggestion(sensor, availableGateways))
                .collect(Collectors.toList());
    }

    /**
     * Create sensor response with suggested gateway
     * @param sensor The unassigned sensor
     * @param availableGateways List of available gateways
     * @return Sensor response with suggestion
     */
    private SensorResponseWithSuggestion createSensorWithSuggestion(
            Sensor sensor, List<Gateway> availableGateways) {

        Gateway closestGateway = findClosestGateway(sensor, availableGateways);

        SensorResponseWithSuggestion.SuggestedGateway suggestion = null;
        if (closestGateway != null) {
            double distance = DistanceUtil.calculateEuclideanDistance(
                    sensor.getXCoordinate(), sensor.getYCoordinate(),
                    closestGateway.getXCoordinate(), closestGateway.getYCoordinate()
            );

            suggestion = SensorResponseWithSuggestion.SuggestedGateway.builder()
                    .id(closestGateway.getId())
                    .name(closestGateway.getName())
                    .xCoordinate(closestGateway.getXCoordinate())
                    .yCoordinate(closestGateway.getYCoordinate())
                    .distance(distance)
                    .build();
        }

        return SensorResponseWithSuggestion.builder()
                .id(sensor.getId())
                .name(sensor.getName())
                .xCoordinate(sensor.getXCoordinate())
                .yCoordinate(sensor.getYCoordinate())
                .createdAt(sensor.getCreatedAt())
                .types(sensor.getTypes().stream()
                        .map(type -> type.getType())
                        .collect(Collectors.toList()))
                .suggestedGateway(suggestion)
                .build();
    }

    /**
     * Find the closest gateway to a sensor
     * @param sensor The sensor to find closest gateway for
     * @param availableGateways List of available gateways
     * @return Closest gateway or null if none available
     */
    private Gateway findClosestGateway(Sensor sensor, List<Gateway> availableGateways) {
        if (availableGateways.isEmpty() ||
                sensor.getXCoordinate() == null ||
                sensor.getYCoordinate() == null) {
            return null;
        }

        Gateway closestGateway = null;
        double minDistance = Double.MAX_VALUE;

        for (Gateway gateway : availableGateways) {
            if (gateway.getXCoordinate() != null && gateway.getYCoordinate() != null) {
                double distance = DistanceUtil.calculateEuclideanDistance(
                        sensor.getXCoordinate(), sensor.getYCoordinate(),
                        gateway.getXCoordinate(), gateway.getYCoordinate()
                );

                if (distance < minDistance) {
                    minDistance = distance;
                    closestGateway = gateway;
                }
            }
        }

        return closestGateway;
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

        // 1) Create gateways with coordinates
        List<Gateway> gateways = IntStream.rangeClosed(1, 5)
                .mapToObj(i -> {
                    Gateway g = new Gateway();
                    g.setName("GW-" + UUID.randomUUID().toString().substring(0, 8));

                    // Generate random coordinates for gateways
                    // Using a coordinate system from 0 to 1000 for both x and y
                    g.setXCoordinate(BigDecimal.valueOf(random.nextDouble() * 1000)
                            .setScale(6, RoundingMode.HALF_UP));
                    g.setYCoordinate(BigDecimal.valueOf(random.nextDouble() * 1000)
                            .setScale(6, RoundingMode.HALF_UP));

                    return gatewayRepo.save(g);
                })
                .toList();

        // 2) Fetch types once
        List<SensorType> types = sensorTypeRepository.findAll();

        // 3) Create and save sensors one by one
        for (int i = 1; i <= 1000; i++) {
            Sensor s = new Sensor();
            s.setName("Sensor-" + i);

            // Generate random coordinates for sensors
            // Using the same coordinate system (0-1000) as gateways
            s.setXCoordinate(BigDecimal.valueOf(random.nextDouble() * 1000)
                    .setScale(6, RoundingMode.HALF_UP));
            s.setYCoordinate(BigDecimal.valueOf(random.nextDouble() * 1000)
                    .setScale(6, RoundingMode.HALF_UP));

            // Assign gateway 80% of the time
//            if (random.nextDouble() < 0.8) {
                Gateway pick = gateways.get(random.nextInt(gateways.size()));
                s.setGateway(pick);
//            }

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

        // Enhanced logging to show coordinates and gateway assignments
        sensorRepo.findAll().forEach(s -> {
            String assigned = s.getTypes().stream()
                    .map(SensorType::getType)
                    .collect(Collectors.joining(", "));

            String gatewayInfo = s.getGateway() != null ?
                    " | Gateway: " + s.getGateway().getName() +
                            " (GW coords: " + s.getGateway().getXCoordinate() + ", " + s.getGateway().getYCoordinate() + ")" :
                    " | No Gateway";

            System.out.println(
                    "Sensor " + s.getName() +
                            " at (" + s.getXCoordinate() + ", " + s.getYCoordinate() + ")" +
                            " has types: " + assigned +
                            gatewayInfo
            );
        });
    }
}
