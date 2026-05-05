package com.skillsync.recommendation.dto;

import java.util.Set;

import com.skillsync.dsa.common.Difficulty;

import lombok.Builder;
import lombok.Getter;

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

