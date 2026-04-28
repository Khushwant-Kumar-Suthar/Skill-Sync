package com.skillsync.roadmap.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoadmapResponse {

    private String            userName;
    private int               totalSteps;
    private int               completedSteps;
    private double            overallProgressPercent;
    private List<RoadmapStepDTO> steps;
}