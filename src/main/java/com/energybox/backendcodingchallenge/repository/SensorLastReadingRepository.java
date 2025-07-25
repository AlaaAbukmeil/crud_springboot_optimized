package com.energybox.backendcodingchallenge.repository;

import com.energybox.backendcodingchallenge.domain.SensorLastReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SensorLastReadingRepository extends JpaRepository<SensorLastReading, Long> {
    List<SensorLastReading> findBySensorId(Long sensorId);

    Optional<SensorLastReading> findBySensorIdAndSensorTypeId(Long sensorId, Long sensorTypeId);

}
