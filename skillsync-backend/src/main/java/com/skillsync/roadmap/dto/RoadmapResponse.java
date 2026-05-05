package com.skillsync.roadmap.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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