package com.ambulance.ambulancedispatch.service;

import com.ambulance.ambulancedispatch.entity.Ambulance;
import com.ambulance.ambulancedispatch.entity.EmergencyCall;
import com.ambulance.ambulancedispatch.entity.Zone;
import com.ambulance.ambulancedispatch.entity.ZoneDistance;
import com.ambulance.ambulancedispatch.repository.AmbulanceRepository;
import com.ambulance.ambulancedispatch.repository.EmergencyCallRepository;
import com.ambulance.ambulancedispatch.repository.ZoneDistanceRepository;
import com.ambulance.ambulancedispatch.repository.ZoneRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class EmergencyCallService {

    private final EmergencyCallRepository emergencyCallRepository;
    private final AmbulanceRepository ambulanceRepository;
    private final ZoneRepository zoneRepository;
    private final ZoneDistanceRepository zoneDistanceRepository;

    public EmergencyCallService(
            EmergencyCallRepository emergencyCallRepository,
            AmbulanceRepository ambulanceRepository,
            ZoneRepository zoneRepository,
            ZoneDistanceRepository zoneDistanceRepository) {

        this.emergencyCallRepository =
                emergencyCallRepository;

        this.ambulanceRepository =
                ambulanceRepository;

        this.zoneRepository =
                zoneRepository;

        this.zoneDistanceRepository =
                zoneDistanceRepository;
    }

    // =========================================================
    // CREATE EMERGENCY CALL
    // =========================================================

    @Transactional
    public EmergencyCall createEmergencyCall(Long zoneId) {

        // -----------------------------------------------------
        // 1. Find emergency zone
        // -----------------------------------------------------

        Zone callerZone =
                zoneRepository.findById(zoneId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Zone not found with id: "
                                                + zoneId
                                ));

        // -----------------------------------------------------
        // 2. Find all available ambulances
        // -----------------------------------------------------

        List<Ambulance> availableAmbulances =
                ambulanceRepository
                        .findByStatus("AVAILABLE");

        if (availableAmbulances.isEmpty()) {

            throw new RuntimeException(
                    "No available ambulance at the moment"
            );
        }

        // -----------------------------------------------------
        // 3. Find nearest ambulance
        //
        // Only ambulances with a configured route are considered.
        // Same-zone ambulance has distance = 0 km.
        // -----------------------------------------------------

        Ambulance nearestAmbulance =
                availableAmbulances.stream()

                        .filter(ambulance ->
                                hasDistanceConfigured(
                                        ambulance.getZone(),
                                        callerZone
                                ))

                        .min(
                                Comparator.comparingDouble(
                                        ambulance ->
                                                calculateDistance(
                                                        ambulance.getZone(),
                                                        callerZone
                                                )
                                )
                        )

                        .orElseThrow(() ->
                                new RuntimeException(
                                        "No available ambulance "
                                                + "has a configured route "
                                                + "to "
                                                + callerZone.getName()
                                ));

        // -----------------------------------------------------
        // 4. Create emergency call
        // -----------------------------------------------------

        EmergencyCall emergencyCall =
                new EmergencyCall();

        emergencyCall.setCallerZone(
                callerZone
        );

        emergencyCall.setAmbulance(
                nearestAmbulance
        );

        LocalDateTime currentTime =
                LocalDateTime.now();

        emergencyCall.setCallTime(
                currentTime
        );

        emergencyCall.setDispatchTime(
                currentTime
        );

        emergencyCall.setStatus(
                "ASSIGNED"
        );

        // -----------------------------------------------------
        // 5. Mark ambulance BUSY
        // -----------------------------------------------------

        nearestAmbulance.setStatus(
                "BUSY"
        );

        ambulanceRepository.save(
                nearestAmbulance
        );

        // -----------------------------------------------------
        // 6. Save emergency call
        // -----------------------------------------------------

        return emergencyCallRepository.save(
                emergencyCall
        );
    }

    // =========================================================
    // CHECK WHETHER ROUTE EXISTS
    // =========================================================

    private boolean hasDistanceConfigured(
            Zone ambulanceZone,
            Zone emergencyZone) {

        if (ambulanceZone == null ||
                emergencyZone == null) {

            return false;
        }

        // Same zone does not require a distance record.
        if (ambulanceZone.getId()
                .equals(emergencyZone.getId())) {

            return true;
        }

        return zoneDistanceRepository
                .findByFromZoneIdAndToZoneId(
                        ambulanceZone.getId(),
                        emergencyZone.getId()
                )
                .isPresent();
    }

    // =========================================================
    // CALCULATE DISTANCE
    // =========================================================

    private double calculateDistance(
            Zone ambulanceZone,
            Zone emergencyZone) {

        // Same zone = 0 km
        if (ambulanceZone.getId()
                .equals(emergencyZone.getId())) {

            return 0.0;
        }

        ZoneDistance distance =
                zoneDistanceRepository
                        .findByFromZoneIdAndToZoneId(
                                ambulanceZone.getId(),
                                emergencyZone.getId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Distance not configured between "
                                                + ambulanceZone.getName()
                                                + " and "
                                                + emergencyZone.getName()
                                ));

        return distance.getDistanceKm();
    }

    // =========================================================
    // GET ALL EMERGENCY CALLS
    // =========================================================

    public List<EmergencyCall> getAllEmergencyCalls() {

        return emergencyCallRepository.findAll();
    }

    // =========================================================
    // GET EMERGENCY CALL BY ID
    // =========================================================

    public EmergencyCall getEmergencyCallById(Long id) {

        return emergencyCallRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Emergency call not found with id: "
                                        + id
                        ));
    }

    // =========================================================
    // MARK AMBULANCE AS ARRIVED
    // =========================================================

    @Transactional
    public EmergencyCall markAmbulanceArrived(Long id) {

        EmergencyCall emergencyCall =
                emergencyCallRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Emergency call not found with id: "
                                                + id
                                ));

        if (!"ASSIGNED".equals(
                emergencyCall.getStatus())) {

            throw new RuntimeException(
                    "Emergency call is not in ASSIGNED status"
            );
        }

        LocalDateTime arrivalTime =
                LocalDateTime.now();

        emergencyCall.setArrivalTime(
                arrivalTime
        );

        // Actual response time:
        // emergency call → ambulance arrival
        long responseTimeMinutes =
                Duration.between(
                        emergencyCall.getCallTime(),
                        arrivalTime
                ).toMinutes();

        emergencyCall.setResponseTimeMinutes(
                responseTimeMinutes
        );

        emergencyCall.setStatus(
                "ARRIVED"
        );

        return emergencyCallRepository.save(
                emergencyCall
        );
    }

    // =========================================================
    // COMPLETE EMERGENCY CALL
    // =========================================================

    @Transactional
    public EmergencyCall completeEmergencyCall(Long id) {

        EmergencyCall emergencyCall =
                emergencyCallRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Emergency call not found with id: "
                                                + id
                                ));

        if (!"ARRIVED".equals(
                emergencyCall.getStatus())) {

            throw new RuntimeException(
                    "Ambulance must arrive before completing "
                            + "the emergency"
            );
        }

        LocalDateTime completionTime =
                LocalDateTime.now();

        emergencyCall.setCompletionTime(
                completionTime
        );

        emergencyCall.setStatus(
                "COMPLETED"
        );

        // -----------------------------------------------------
        // Make ambulance available again
        // -----------------------------------------------------

        Ambulance ambulance =
                emergencyCall.getAmbulance();

        if (ambulance != null) {

            ambulance.setStatus(
                    "AVAILABLE"
            );

            ambulanceRepository.save(
                    ambulance
            );
        }

        return emergencyCallRepository.save(
                emergencyCall
        );
    }
}