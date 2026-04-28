package com.skillsync.admin.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminStatsDTO {

    private long   totalUsers;
    private long   totalSkills;
    private long   totalCategories;
    private long   totalActivitiesLogged;
    private long   totalMinutesPracticed;
    private double platformAverageProgress;  // avg progress across all users
}
