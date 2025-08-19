package com.energybox.backendcodingchallenge.repository;

import com.energybox.backendcodingchallenge.domain.Gateway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface GatewayRepository extends JpaRepository<Gateway, Long> {

//                --> sensor 1    --> temperature
//                                --> electricity
//
// Gateway 1(OtM) --> sensor 2
//                                 --> temperature
//                                 --> pressure
//
//                --> sensor 3     --> humidity
//SELECT DISTINCT g.*
//    FROM devices.gateways g
//    INNER JOIN sensors s ON g.id = s.gateway_id
//    INNER JOIN sensor_sensor_types sst ON s.id = sst.sensor_id
//    INNER JOIN sensor_types st ON sst.sensor_type_id = st.id
//    WHERE st.type = ?
    List<Gateway> findDistinctBySensors_Types_Type(String type);

    @Query("SELECT DISTINCT g FROM Gateway g " +
            "JOIN g.sensors s " +
            "JOIN s.types t " +
            "WHERE t.type = :type")
    List<Gateway> findGatewaysByType(String type);
}
