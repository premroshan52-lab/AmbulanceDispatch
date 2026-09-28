package com.ambulance.ambulancedispatch.controller;

import com.ambulance.ambulancedispatch.entity.ZoneDistance;
import com.ambulance.ambulancedispatch.service.ZoneDistanceService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zone-distances")
public class ZoneDistanceController {

    private final ZoneDistanceService zoneDistanceService;

    public ZoneDistanceController(
            ZoneDistanceService zoneDistanceService) {

        this.zoneDistanceService = zoneDistanceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ZoneDistance createDistance(
            @Valid @RequestBody ZoneDistance zoneDistance) {

        return zoneDistanceService.createDistance(zoneDistance);
    }

    @GetMapping
    public List<ZoneDistance> getAllDistances() {

        return zoneDistanceService.getAllDistances();
    }

    @GetMapping("/{id}")
    public ZoneDistance getDistanceById(
            @PathVariable Long id) {

        return zoneDistanceService.getDistanceById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDistance(
            @PathVariable Long id) {

        zoneDistanceService.deleteDistance(id);
    }
}