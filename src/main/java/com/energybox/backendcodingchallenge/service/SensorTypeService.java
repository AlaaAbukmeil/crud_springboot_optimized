package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.domain.SensorType;
import com.energybox.backendcodingchallenge.repository.SensorRepository;
import com.energybox.backendcodingchallenge.repository.SensorTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class SensorTypeService {
    private final SensorTypeRepository typeRepo;
    private final SensorRepository sensorRepo;

    public SensorType create(SensorType sensorType) {
        return typeRepo.save(sensorType);
    }

    public void delete(Long typeId) {
        SensorType toDelete = typeRepo.findById(typeId)
                .orElseThrow(() -> new EntityNotFoundException("Sensor Type with id " + typeId + " not found"));

        List<Sensor> sensors = sensorRepo.findByTypes_Type(toDelete.getType());

        sensors.forEach(sensor -> {
            sensor.getTypes().remove(toDelete);
        });

        sensorRepo.saveAll(sensors);

        typeRepo.delete(toDelete);
    }

    public SensorType findSensorTypeByName(String typeName) {

        return typeRepo.findByType(typeName)
                .orElseThrow(() -> new EntityNotFoundException("Sensor Type with typename " + typeName + " not found"));
    }

}
