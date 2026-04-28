package com.skillsync.recommendation.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationDTO {

    private Long   skillId;
    private String skillName;
    private Double currentProgress;
    private Double currentScore;
    private String reason;
    private String priority;         // HIGH / MEDIUM / LOW
    private String lastPracticedAt;
}
