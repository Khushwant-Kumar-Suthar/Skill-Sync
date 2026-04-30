package com.skillsync.dsa.plan.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlanProgressDTO {
    private String planSlug;
    private String planName;
    private long totalProblems;
    private long solved;
    private long attempted;
    private long remaining;
    private double completionPercent;
}

