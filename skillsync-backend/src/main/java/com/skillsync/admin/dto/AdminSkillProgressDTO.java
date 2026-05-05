package com.skillsync.admin.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminSkillProgressDTO {
    private Long userId;
    private String userName;
    private String userEmail;
    private Long skillId;
    private String skillName;
    private String categoryName;
    private double progressPercentage;
    private double score;
    private String lastPracticedAt;
}
