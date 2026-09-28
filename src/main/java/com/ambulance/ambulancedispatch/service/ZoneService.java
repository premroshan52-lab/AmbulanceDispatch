package com.ambulance.ambulancedispatch.service;

import com.ambulance.ambulancedispatch.entity.Zone;
import com.ambulance.ambulancedispatch.repository.ZoneRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneService {

    private final ZoneRepository zoneRepository;

    public ZoneService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    public Zone createZone(Zone zone) {
        return zoneRepository.save(zone);
    }

    public List<Zone> getAllZones() {
        return zoneRepository.findAll();
    }

    public Zone getZoneById(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Zone not found with id: " + id));
    }

    public void deleteZone(Long id) {
        if (!zoneRepository.existsById(id)) {
            throw new RuntimeException("Zone not found with id: " + id);
        }

        zoneRepository.deleteById(id);
    }
}