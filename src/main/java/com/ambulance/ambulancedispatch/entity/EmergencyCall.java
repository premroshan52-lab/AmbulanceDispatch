package com.ambulance.ambulancedispatch.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "emergency_calls")
public class EmergencyCall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Zone where emergency call was received
    @ManyToOne
    @JoinColumn(name = "caller_zone_id", nullable = false)
    private Zone callerZone;

    // Ambulance assigned to this emergency
    @ManyToOne
    @JoinColumn(name = "ambulance_id")
    private Ambulance ambulance;

    // Time when emergency call was received
    @Column(nullable = false)
    private LocalDateTime callTime;

    // Time when ambulance was dispatched
    private LocalDateTime dispatchTime;

    // Time when ambulance reached the emergency location
    private LocalDateTime arrivalTime;

    // Time when emergency was completed
    private LocalDateTime completionTime;

    // Actual response time:
    // arrivalTime - callTime
    private Long responseTimeMinutes;

    // ASSIGNED / ARRIVED / COMPLETED
    @Column(nullable = false)
    private String status;

    public EmergencyCall() {
    }

    public Long getId() {
        return id;
    }

    public Zone getCallerZone() {
        return callerZone;
    }

    public void setCallerZone(Zone callerZone) {
        this.callerZone = callerZone;
    }

    public Ambulance getAmbulance() {
        return ambulance;
    }

    public void setAmbulance(Ambulance ambulance) {
        this.ambulance = ambulance;
    }

    public LocalDateTime getCallTime() {
        return callTime;
    }

    public void setCallTime(LocalDateTime callTime) {
        this.callTime = callTime;
    }

    public LocalDateTime getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(LocalDateTime dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalDateTime getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(LocalDateTime completionTime) {
        this.completionTime = completionTime;
    }

    public Long getResponseTimeMinutes() {
        return responseTimeMinutes;
    }

    public void setResponseTimeMinutes(Long responseTimeMinutes) {
        this.responseTimeMinutes = responseTimeMinutes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}