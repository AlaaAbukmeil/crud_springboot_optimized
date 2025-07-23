package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.repository.GatewayRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GatewayService {
    private final GatewayRepository repo;



    public List<Gateway> findAll() {
        return repo.findAll();
    }

    public Gateway create(Gateway gateway) {
        return repo.save(gateway);
    }

    public List<Gateway> findWithSensorType(String type) {
        return repo.findDistinctBySensors_Types_Type(type);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

}
