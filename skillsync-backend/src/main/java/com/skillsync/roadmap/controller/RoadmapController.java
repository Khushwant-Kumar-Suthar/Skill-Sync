package com.skillsync.roadmap.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.skillsync.common.response.ApiResponse;
import com.skillsync.common.util.ResponseUtil;
import com.skillsync.roadmap.dto.GenerateRoadmapRequest;
import com.skillsync.roadmap.dto.RoadmapResponse;
import com.skillsync.roadmap.service.RoadmapService;

@RestController
@RequestMapping("/api/roadmap")
@RequiredArgsConstructor
public class RoadmapController {

    private final RoadmapService roadmapService;

    /**
     * GET /api/roadmap
     * Returns the current user's roadmap.
     * Auto-generates one at 60 min/day if none exists or if stale.
     */
    @GetMapping
    public ApiResponse<RoadmapResponse> getRoadmap() {
        return ResponseUtil.success(
                "Roadmap fetched successfully",
                roadmapService.getRoadmap());
    }

    /**
     * POST /api/roadmap/generate
     * Explicitly regenerates the roadmap using the user's daily availability.
     * Always creates a fresh roadmap — previous steps are deleted.
     *
     * Body (optional fields — all have defaults):
     * {
     *   "dailyMinutesAvailable": 90
     * }
     */
    @PostMapping("/generate")
    public ApiResponse<RoadmapResponse> generateRoadmap(
            @Valid @RequestBody(required = false)
            GenerateRoadmapRequest request) {

        if (request == null) {
            request = new GenerateRoadmapRequest(); // use defaults
        }

        return ResponseUtil.success(
                "Roadmap generated successfully",
                roadmapService.generateRoadmap(request));
    }

    /**
     * PATCH /api/roadmap/steps/{stepId}/complete
     * Marks a roadmap step as completed.
     */
    @PatchMapping("/steps/{stepId}/complete")
    public ApiResponse<Void> markStepCompleted(
            @PathVariable Long stepId) {

        roadmapService.markStepCompleted(stepId);
        return ResponseUtil.successMessage("Step marked as completed");
    }
}