package com.ambulance.ambulancedispatch.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(
    name = "zone_distances",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"from_zone_id", "to_zone_id"}
        )
    }
)
public class ZoneDistance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "From zone is required")
    @ManyToOne
    @JoinColumn(name = "from_zone_id", nullable = false)
    private Zone fromZone;

    @NotNull(message = "To zone is required")
    @ManyToOne
    @JoinColumn(name = "to_zone_id", nullable = false)
    private Zone toZone;

    @NotNull(message = "Distance is required")
    @PositiveOrZero(message = "Distance cannot be negative")
    @Column(nullable = false)
    private Double distanceKm;

    public ZoneDistance() {
    }

    public Long getId() {
        return id;
    }

    public Zone getFromZone() {
        return fromZone;
    }

    public void setFromZone(Zone fromZone) {
        this.fromZone = fromZone;
    }

    public Zone getToZone() {
        return toZone;
    }

    public void setToZone(Zone toZone) {
        this.toZone = toZone;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }
}