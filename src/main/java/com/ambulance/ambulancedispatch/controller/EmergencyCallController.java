package com.ambulance.ambulancedispatch.controller;

import com.ambulance.ambulancedispatch.entity.EmergencyCall;
import com.ambulance.ambulancedispatch.service.EmergencyCallService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/emergency-calls")
public class EmergencyCallController {

    private final EmergencyCallService emergencyCallService;

    public EmergencyCallController(
            EmergencyCallService emergencyCallService) {

        this.emergencyCallService =
                emergencyCallService;
    }

    // =========================================================
    // CREATE EMERGENCY CALL
    // =========================================================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmergencyCall createEmergencyCall(
            @RequestBody Map<String, Long> request) {

        Long zoneId = request.get("zoneId");

        if (zoneId == null) {

            throw new RuntimeException(
                    "zoneId is required"
            );
        }

        return emergencyCallService
                .createEmergencyCall(zoneId);
    }

    // =========================================================
    // GET ALL EMERGENCY CALLS
    // =========================================================

    @GetMapping
    public List<EmergencyCall> getAllEmergencyCalls() {

        return emergencyCallService
                .getAllEmergencyCalls();
    }

    // =========================================================
    // GET EMERGENCY CALL BY ID
    // =========================================================

    @GetMapping("/{id}")
    public EmergencyCall getEmergencyCallById(
            @PathVariable Long id) {

        return emergencyCallService
                .getEmergencyCallById(id);
    }

    // =========================================================
    // MARK AMBULANCE AS ARRIVED
    // =========================================================

    @PutMapping("/{id}/arrive")
    public EmergencyCall markAmbulanceArrived(
            @PathVariable Long id) {

        return emergencyCallService
                .markAmbulanceArrived(id);
    }

    // =========================================================
    // COMPLETE EMERGENCY CALL
    // =========================================================

    @PutMapping("/{id}/complete")
    public EmergencyCall completeEmergencyCall(
            @PathVariable Long id) {

        return emergencyCallService
                .completeEmergencyCall(id);
    }
}