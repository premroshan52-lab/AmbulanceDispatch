package com.ambulance.ambulancedispatch.repository;

import com.ambulance.ambulancedispatch.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepository extends JpaRepository<Zone, Long> {
}