package com.skillsync.recommendation.dto;

import com.skillsync.dsa.common.Difficulty;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class ProblemRecommendationDTO {
    private Long problemId;
    private String title;
    private String slug;
    private Difficulty difficulty;
    private String reason;
    private Set<String> tags;
}

