package com.ambulance.ambulancedispatch.service;

import com.ambulance.ambulancedispatch.entity.ZoneDistance;
import com.ambulance.ambulancedispatch.repository.ZoneDistanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneDistanceService {

    private final ZoneDistanceRepository zoneDistanceRepository;

    public ZoneDistanceService(
            ZoneDistanceRepository zoneDistanceRepository) {

        this.zoneDistanceRepository = zoneDistanceRepository;
    }

    public ZoneDistance createDistance(
            ZoneDistance zoneDistance) {

        if (zoneDistance.getFromZone().getId()
                .equals(zoneDistance.getToZone().getId())) {

            throw new RuntimeException(
                    "From zone and To zone cannot be the same"
            );
        }

        return zoneDistanceRepository.save(zoneDistance);
    }

    public List<ZoneDistance> getAllDistances() {
        return zoneDistanceRepository.findAll();
    }

    public ZoneDistance getDistanceById(Long id) {

        return zoneDistanceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Zone distance not found with id: " + id
                        ));
    }

    public void deleteDistance(Long id) {

        if (!zoneDistanceRepository.existsById(id)) {
            throw new RuntimeException(
                    "Zone distance not found with id: " + id
            );
        }

        zoneDistanceRepository.deleteById(id);
    }
}