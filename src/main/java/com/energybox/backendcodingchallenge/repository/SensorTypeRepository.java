package com.energybox.backendcodingchallenge.repository;

import com.energybox.backendcodingchallenge.domain.SensorType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SensorTypeRepository extends JpaRepository<SensorType, Long> {
//    find if there is a type
 Optional<SensorType> findByType(String type);

}

