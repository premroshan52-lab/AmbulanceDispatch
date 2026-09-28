package com.ambulance.ambulancedispatch.dto;

public class ZoneAnalyticsResponse {

    private Long zoneId;

    private String zoneName;

    private long totalCalls;

    private long completedCalls;

    private long activeCalls;

    private double averageResponseTimeMinutes;

    public ZoneAnalyticsResponse() {
    }

    public ZoneAnalyticsResponse(
            Long zoneId,
            String zoneName,
            long totalCalls,
            long completedCalls,
            long activeCalls,
            double averageResponseTimeMinutes) {

        this.zoneId = zoneId;
        this.zoneName = zoneName;
        this.totalCalls = totalCalls;
        this.completedCalls = completedCalls;
        this.activeCalls = activeCalls;
        this.averageResponseTimeMinutes =
                averageResponseTimeMinutes;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public long getTotalCalls() {
        return totalCalls;
    }

    public void setTotalCalls(long totalCalls) {
        this.totalCalls = totalCalls;
    }

    public long getCompletedCalls() {
        return completedCalls;
    }

    public void setCompletedCalls(long completedCalls) {
        this.completedCalls = completedCalls;
    }

    public long getActiveCalls() {
        return activeCalls;
    }

    public void setActiveCalls(long activeCalls) {
        this.activeCalls = activeCalls;
    }

    public double getAverageResponseTimeMinutes() {
        return averageResponseTimeMinutes;
    }

    public void setAverageResponseTimeMinutes(
            double averageResponseTimeMinutes) {

        this.averageResponseTimeMinutes =
                averageResponseTimeMinutes;
    }
}