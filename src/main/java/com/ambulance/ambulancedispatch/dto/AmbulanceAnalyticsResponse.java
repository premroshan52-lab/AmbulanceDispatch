package com.ambulance.ambulancedispatch.dto;

public class AmbulanceAnalyticsResponse {

    private Long ambulanceId;

    private String ambulanceNumber;

    private String zoneName;

    private String status;

    private long emergencyCallsHandled;

    public AmbulanceAnalyticsResponse() {
    }

    public AmbulanceAnalyticsResponse(
            Long ambulanceId,
            String ambulanceNumber,
            String zoneName,
            String status,
            long emergencyCallsHandled) {

        this.ambulanceId = ambulanceId;
        this.ambulanceNumber = ambulanceNumber;
        this.zoneName = zoneName;
        this.status = status;
        this.emergencyCallsHandled =
                emergencyCallsHandled;
    }

    public Long getAmbulanceId() {
        return ambulanceId;
    }

    public void setAmbulanceId(Long ambulanceId) {
        this.ambulanceId = ambulanceId;
    }

    public String getAmbulanceNumber() {
        return ambulanceNumber;
    }

    public void setAmbulanceNumber(
            String ambulanceNumber) {

        this.ambulanceNumber = ambulanceNumber;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getEmergencyCallsHandled() {
        return emergencyCallsHandled;
    }

    public void setEmergencyCallsHandled(
            long emergencyCallsHandled) {

        this.emergencyCallsHandled =
                emergencyCallsHandled;
    }
}