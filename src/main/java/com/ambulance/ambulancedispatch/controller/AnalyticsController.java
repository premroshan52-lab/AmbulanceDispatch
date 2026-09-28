package com.ambulance.ambulancedispatch.controller;

import com.ambulance.ambulancedispatch.dto.AnalyticsResponse;
import com.ambulance.ambulancedispatch.dto.AmbulanceAnalyticsResponse;
import com.ambulance.ambulancedispatch.dto.DashboardResponse;
import com.ambulance.ambulancedispatch.dto.ZoneAnalyticsResponse;
import com.ambulance.ambulancedispatch.service.AnalyticsService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(
            AnalyticsService analyticsService) {

        this.analyticsService =
                analyticsService;
    }

    // =========================================================
    // RESPONSE TIME ANALYTICS
    // =========================================================

    @GetMapping("/response-time")
    public AnalyticsResponse getResponseTimeAnalytics() {

        return analyticsService
                .getResponseTimeAnalytics();
    }

    // =========================================================
    // ZONE RESPONSE TIME ANALYTICS
    // =========================================================

    @GetMapping("/zone-response-time")
    public List<ZoneAnalyticsResponse>
    getZoneResponseTimeAnalytics() {

        return analyticsService
                .getZoneResponseTimeAnalytics();
    }

    // =========================================================
    // AMBULANCE ANALYTICS
    // =========================================================

    @GetMapping("/ambulances")
    public List<AmbulanceAnalyticsResponse>
    getAmbulanceAnalytics() {

        return analyticsService
                .getAmbulanceAnalytics();
    }

    // =========================================================
    // TOTAL AMBULANCES
    // =========================================================

    @GetMapping("/ambulances/total")
    public long getTotalAmbulances() {

        return analyticsService
                .getTotalAmbulances();
    }

    // =========================================================
    // AVAILABLE AMBULANCES
    // =========================================================

    @GetMapping("/ambulances/available")
    public long getAvailableAmbulances() {

        return analyticsService
                .getAvailableAmbulances();
    }

    // =========================================================
    // BUSY AMBULANCES
    // =========================================================

    @GetMapping("/ambulances/busy")
    public long getBusyAmbulances() {

        return analyticsService
                .getBusyAmbulances();
    }

    // =========================================================
    // COMPLETE DASHBOARD
    // =========================================================

    @GetMapping("/dashboard")
    public DashboardResponse getDashboardData() {

        return analyticsService
                .getDashboardData();
    }
}