package com.skillsync.roadmap.service;

import com.skillsync.roadmap.dto.GenerateRoadmapRequest;
import com.skillsync.roadmap.dto.RoadmapResponse;

public interface RoadmapService {

    /** Returns the current user's roadmap. Auto-generates one if none exists. */
    RoadmapResponse getRoadmap();

    /**
     * Explicitly regenerates the roadmap using the user's daily availability.
     * Deletes the existing roadmap and creates a fresh one.
     */
    RoadmapResponse generateRoadmap(GenerateRoadmapRequest request);

    /** Marks a single roadmap step as completed. */
    void markStepCompleted(Long stepId);
}