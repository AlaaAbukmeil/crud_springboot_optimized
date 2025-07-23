package com.energybox.backendcodingchallenge.repository;

import com.energybox.backendcodingchallenge.domain.Gateway;
import org.springframework.data.jpa.repository.JpaRepository;

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
    List<Gateway> findDistinctBySensors_Types_Type(String type);
}
