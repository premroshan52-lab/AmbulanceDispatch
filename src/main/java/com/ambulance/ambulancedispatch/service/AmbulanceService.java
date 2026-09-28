package com.ambulance.ambulancedispatch.service;

import com.ambulance.ambulancedispatch.entity.Ambulance;
import com.ambulance.ambulancedispatch.repository.AmbulanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AmbulanceService {

    private final AmbulanceRepository ambulanceRepository;

    public AmbulanceService(AmbulanceRepository ambulanceRepository) {
        this.ambulanceRepository = ambulanceRepository;
    }

    public Ambulance createAmbulance(Ambulance ambulance) {

        if (ambulance.getStatus() == null ||
                ambulance.getStatus().isBlank()) {

            ambulance.setStatus("AVAILABLE");
        }

        return ambulanceRepository.save(ambulance);
    }

    public List<Ambulance> getAllAmbulances() {
        return ambulanceRepository.findAll();
    }

    public Ambulance getAmbulanceById(Long id) {

        return ambulanceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Ambulance not found with id: " + id
                        ));
    }

    public Ambulance updateStatus(Long id, String status) {

        Ambulance ambulance = getAmbulanceById(id);

        if (!status.equals("AVAILABLE") &&
                !status.equals("BUSY")) {

            throw new RuntimeException(
                    "Status must be AVAILABLE or BUSY"
            );
        }

        ambulance.setStatus(status);

        return ambulanceRepository.save(ambulance);
    }
}