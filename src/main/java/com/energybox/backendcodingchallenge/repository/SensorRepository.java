package com.energybox.backendcodingchallenge.repository;
import com.energybox.backendcodingchallenge.domain.Sensor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
//    look through gateway for id
    List<Sensor> findByGatewayId(Long gatewayId);


//    sensor 1 --> humidity --> return matching type
//             --> pressure
    List<Sensor> findByTypes_Type(String type);
    List<Sensor> findByGatewayIsNull(Sort sort);
}
