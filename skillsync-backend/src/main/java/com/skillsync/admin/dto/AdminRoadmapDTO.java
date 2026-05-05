package com.skillsync.admin.dto;

import java.util.List;

import com.skillsync.roadmap.dto.RoadmapStepDTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminRoadmapDTO {
    private Long userId;
    private String userName;
    private String userEmail;
    private int totalSteps;
    private int completedSteps;
    private double overallProgressPercent;
    private List<RoadmapStepDTO> steps;
}
