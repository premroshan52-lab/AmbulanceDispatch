package com.ambulance.ambulancedispatch.service;

import com.ambulance.ambulancedispatch.dto.AnalyticsResponse;
import com.ambulance.ambulancedispatch.dto.AmbulanceAnalyticsResponse;
import com.ambulance.ambulancedispatch.dto.DashboardResponse;
import com.ambulance.ambulancedispatch.dto.ZoneAnalyticsResponse;
import com.ambulance.ambulancedispatch.entity.Ambulance;
import com.ambulance.ambulancedispatch.entity.EmergencyCall;
import com.ambulance.ambulancedispatch.entity.Zone;
import com.ambulance.ambulancedispatch.repository.AmbulanceRepository;
import com.ambulance.ambulancedispatch.repository.EmergencyCallRepository;
import com.ambulance.ambulancedispatch.repository.ZoneRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnalyticsService {

    private final EmergencyCallRepository emergencyCallRepository;
    private final ZoneRepository zoneRepository;
    private final AmbulanceRepository ambulanceRepository;

    public AnalyticsService(
            EmergencyCallRepository emergencyCallRepository,
            ZoneRepository zoneRepository,
            AmbulanceRepository ambulanceRepository) {

        this.emergencyCallRepository =
                emergencyCallRepository;

        this.zoneRepository =
                zoneRepository;

        this.ambulanceRepository =
                ambulanceRepository;
    }

    // =========================================================
    // OVERALL RESPONSE-TIME ANALYTICS
    // =========================================================

    public AnalyticsResponse getResponseTimeAnalytics() {

        List<EmergencyCall> allCalls =
                emergencyCallRepository.findAll();

        long totalEmergencyCalls =
                allCalls.size();

        List<EmergencyCall> completedCalls =
                emergencyCallRepository
                        .findByStatus("COMPLETED");

        long completedCount =
                completedCalls.size();

        long activeCalls =
                totalEmergencyCalls - completedCount;

        if (completedCalls.isEmpty()) {

            return new AnalyticsResponse(
                    totalEmergencyCalls,
                    0,
                    activeCalls,
                    0.0,
                    0,
                    0
            );
        }

        long totalResponseTime = 0;

        long fastestResponseTime =
                Long.MAX_VALUE;

        long slowestResponseTime =
                Long.MIN_VALUE;

        long validResponseCount = 0;

        for (EmergencyCall call :
                completedCalls) {

            Long responseTime =
                    call.getResponseTimeMinutes();

            if (responseTime != null) {

                totalResponseTime += responseTime;

                validResponseCount++;

                if (responseTime <
                        fastestResponseTime) {

                    fastestResponseTime =
                            responseTime;
                }

                if (responseTime >
                        slowestResponseTime) {

                    slowestResponseTime =
                            responseTime;
                }
            }
        }

        if (validResponseCount == 0) {

            return new AnalyticsResponse(
                    totalEmergencyCalls,
                    completedCount,
                    activeCalls,
                    0.0,
                    0,
                    0
            );
        }

        double averageResponseTime =
                (double) totalResponseTime
                        / validResponseCount;

        return new AnalyticsResponse(
                totalEmergencyCalls,
                completedCount,
                activeCalls,
                averageResponseTime,
                fastestResponseTime,
                slowestResponseTime
        );
    }

    // =========================================================
    // ZONE-WISE RESPONSE-TIME ANALYTICS
    // =========================================================

    public List<ZoneAnalyticsResponse>
    getZoneResponseTimeAnalytics() {

        List<Zone> zones =
                zoneRepository.findAll();

        List<EmergencyCall> allCalls =
                emergencyCallRepository.findAll();

        List<ZoneAnalyticsResponse> results =
                new ArrayList<>();

        for (Zone zone : zones) {

            long totalCalls = 0;
            long completedCalls = 0;
            long activeCalls = 0;
            long totalResponseTime = 0;
            long validResponseCount = 0;

            for (EmergencyCall call :
                    allCalls) {

                if (call.getCallerZone() != null
                        && call.getCallerZone()
                        .getId()
                        .equals(zone.getId())) {

                    totalCalls++;

                    if ("COMPLETED".equals(
                            call.getStatus())) {

                        completedCalls++;

                        if (call.getResponseTimeMinutes()
                                != null) {

                            totalResponseTime +=
                                    call.getResponseTimeMinutes();

                            validResponseCount++;
                        }

                    } else {

                        activeCalls++;
                    }
                }
            }

            double averageResponseTime = 0.0;

            if (validResponseCount > 0) {

                averageResponseTime =
                        (double) totalResponseTime
                                / validResponseCount;
            }

            ZoneAnalyticsResponse result =
                    new ZoneAnalyticsResponse(
                            zone.getId(),
                            zone.getName(),
                            totalCalls,
                            completedCalls,
                            activeCalls,
                            averageResponseTime
                    );

            results.add(result);
        }

        return results;
    }

    // =========================================================
    // AMBULANCE UTILIZATION ANALYTICS
    // =========================================================

    public List<AmbulanceAnalyticsResponse>
    getAmbulanceAnalytics() {

        List<Ambulance> ambulances =
                ambulanceRepository.findAll();

        List<EmergencyCall> allCalls =
                emergencyCallRepository.findAll();

        List<AmbulanceAnalyticsResponse> results =
                new ArrayList<>();

        for (Ambulance ambulance :
                ambulances) {

            long emergencyCallsHandled = 0;

            for (EmergencyCall call :
                    allCalls) {

                if (call.getAmbulance() != null
                        && call.getAmbulance()
                        .getId()
                        .equals(ambulance.getId())) {

                    emergencyCallsHandled++;
                }
            }

            String zoneName = "Unknown";

            if (ambulance.getZone() != null) {

                zoneName =
                        ambulance.getZone().getName();
            }

            AmbulanceAnalyticsResponse result =
                    new AmbulanceAnalyticsResponse(
                            ambulance.getId(),
                            ambulance.getAmbulanceNumber(),
                            zoneName,
                            ambulance.getStatus(),
                            emergencyCallsHandled
                    );

            results.add(result);
        }

        return results;
    }

    // =========================================================
    // AMBULANCE SUMMARY
    // =========================================================

    public long getTotalAmbulances() {

        return ambulanceRepository.count();
    }

    public long getAvailableAmbulances() {

        return ambulanceRepository
                .findByStatus("AVAILABLE")
                .size();
    }

    public long getBusyAmbulances() {

        return ambulanceRepository
                .findByStatus("BUSY")
                .size();
    }

    // =========================================================
    // DASHBOARD DATA
    // =========================================================

    public DashboardResponse getDashboardData() {

        // -----------------------------------------
        // Ambulance statistics
        // -----------------------------------------

        long totalAmbulances =
                getTotalAmbulances();

        long availableAmbulances =
                getAvailableAmbulances();

        long busyAmbulances =
                getBusyAmbulances();

        // -----------------------------------------
        // Emergency call statistics
        // -----------------------------------------

        AnalyticsResponse responseTimeAnalytics =
                getResponseTimeAnalytics();

        long totalEmergencyCalls =
                responseTimeAnalytics
                        .getTotalEmergencyCalls();

        long completedCalls =
                responseTimeAnalytics
                        .getCompletedCalls();

        long activeCalls =
                responseTimeAnalytics
                        .getActiveCalls();

        double averageResponseTime =
                responseTimeAnalytics
                        .getAverageResponseTimeMinutes();

        long fastestResponseTime =
                responseTimeAnalytics
                        .getFastestResponseTimeMinutes();

        long slowestResponseTime =
                responseTimeAnalytics
                        .getSlowestResponseTimeMinutes();

        // -----------------------------------------
        // Build dashboard response
        // -----------------------------------------

        return new DashboardResponse(
                totalAmbulances,
                availableAmbulances,
                busyAmbulances,
                totalEmergencyCalls,
                completedCalls,
                activeCalls,
                averageResponseTime,
                fastestResponseTime,
                slowestResponseTime
        );
    }
}