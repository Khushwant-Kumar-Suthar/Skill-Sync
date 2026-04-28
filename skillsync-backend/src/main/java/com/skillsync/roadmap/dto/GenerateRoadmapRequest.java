package com.skillsync.roadmap.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

/**
 * Request body for POST /api/roadmap/generate
 *
 * Allows the user to explicitly request a fresh roadmap.
 * dailyMinutesAvailable — how many minutes per day the user can dedicate.
 * This is used by RoadmapGenerator to scale estimated days per step.
 *
 * If not provided (GET /api/roadmap), defaults of 60 minutes/day are used.
 */
@Getter
@Setter
public class GenerateRoadmapRequest {

    /**
     * How many minutes per day the user can practice.
     * Used to scale estimated completion days per roadmap step.
     * Default: 60 minutes/day if not supplied.
     */
    @Min(value = 10,  message = "Daily minutes must be at least 10")
    @Max(value = 480, message = "Daily minutes cannot exceed 480 (8 hours)")
    private Integer dailyMinutesAvailable = 60;
}