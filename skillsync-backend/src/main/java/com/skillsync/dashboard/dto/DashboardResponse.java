package com.skillsync.dashboard.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {

    private String userName;

    private int    totalSkillsTracked;
    private int    skillsMastered;
    private int    skillsInProgress;
    private int    skillsNotStarted;

    private double totalScore;
    private double averageProgress;

    private int    totalActivitiesLogged;

    // FIX: was int — must be Long to match repository return type and avoid overflow.
    private long   totalMinutesPracticed;

    private String topSkillName;
    private double topSkillProgress;

    private List<SkillSummary> skillBreakdown;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SkillSummary {
        private Long   skillId;
        private String skillName;
        private double progressPercentage;
        private double score;
        private String lastPracticedAt;
    }
}