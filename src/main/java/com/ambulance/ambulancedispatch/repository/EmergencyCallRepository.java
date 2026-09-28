package com.ambulance.ambulancedispatch.repository;

import com.ambulance.ambulancedispatch.entity.EmergencyCall;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmergencyCallRepository
        extends JpaRepository<EmergencyCall, Long> {

    List<EmergencyCall> findByStatus(String status);

}