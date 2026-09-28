package com.ambulance.ambulancedispatch.dto;

public class AnalyticsResponse {

    private long totalEmergencyCalls;

    private long completedCalls;

    private long activeCalls;

    private double averageResponseTimeMinutes;

    private long fastestResponseTimeMinutes;

    private long slowestResponseTimeMinutes;

    public AnalyticsResponse() {
    }

    public AnalyticsResponse(
            long totalEmergencyCalls,
            long completedCalls,
            long activeCalls,
            double averageResponseTimeMinutes,
            long fastestResponseTimeMinutes,
            long slowestResponseTimeMinutes) {

        this.totalEmergencyCalls = totalEmergencyCalls;
        this.completedCalls = completedCalls;
        this.activeCalls = activeCalls;
        this.averageResponseTimeMinutes =
                averageResponseTimeMinutes;
        this.fastestResponseTimeMinutes =
                fastestResponseTimeMinutes;
        this.slowestResponseTimeMinutes =
                slowestResponseTimeMinutes;
    }

    public long getTotalEmergencyCalls() {
        return totalEmergencyCalls;
    }

    public void setTotalEmergencyCalls(
            long totalEmergencyCalls) {
        this.totalEmergencyCalls =
                totalEmergencyCalls;
    }

    public long getCompletedCalls() {
        return completedCalls;
    }

    public void setCompletedCalls(
            long completedCalls) {
        this.completedCalls = completedCalls;
    }

    public long getActiveCalls() {
        return activeCalls;
    }

    public void setActiveCalls(
            long activeCalls) {
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

    public long getFastestResponseTimeMinutes() {
        return fastestResponseTimeMinutes;
    }

    public void setFastestResponseTimeMinutes(
            long fastestResponseTimeMinutes) {
        this.fastestResponseTimeMinutes =
                fastestResponseTimeMinutes;
    }

    public long getSlowestResponseTimeMinutes() {
        return slowestResponseTimeMinutes;
    }

    public void setSlowestResponseTimeMinutes(
            long slowestResponseTimeMinutes) {
        this.slowestResponseTimeMinutes =
                slowestResponseTimeMinutes;
    }
}