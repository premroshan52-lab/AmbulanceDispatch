package com.ambulance.ambulancedispatch.controller;

import com.ambulance.ambulancedispatch.entity.Ambulance;
import com.ambulance.ambulancedispatch.service.AmbulanceService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ambulances")
public class AmbulanceController {

    private final AmbulanceService ambulanceService;

    public AmbulanceController(AmbulanceService ambulanceService) {
        this.ambulanceService = ambulanceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Ambulance createAmbulance(
            @Valid @RequestBody Ambulance ambulance) {

        return ambulanceService.createAmbulance(ambulance);
    }

    @GetMapping
    public List<Ambulance> getAllAmbulances() {

        return ambulanceService.getAllAmbulances();
    }

    @GetMapping("/{id}")
    public Ambulance getAmbulanceById(
            @PathVariable Long id) {

        return ambulanceService.getAmbulanceById(id);
    }

    @PutMapping("/{id}/status")
    public Ambulance updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return ambulanceService.updateStatus(id, status);
    }
}