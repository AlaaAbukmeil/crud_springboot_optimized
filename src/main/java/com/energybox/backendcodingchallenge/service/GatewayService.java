package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.repository.GatewayRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GatewayService {
    private final GatewayRepository repo;

    public GatewayService(GatewayRepository repo) {
        this.repo = repo;
    }

    public List<Gateway> findAll() {
        return repo.findAll();
    }

    public Gateway create(Gateway gateway) {
        return repo.save(gateway);
    }

    public List<Gateway> findWithSensorType(String type) {
        return repo.findDistinctBySensors_Types_Type(type);
    }
}
