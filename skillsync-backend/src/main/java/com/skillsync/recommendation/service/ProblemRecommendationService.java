package com.skillsync.recommendation.service;

import java.util.List;

import com.skillsync.recommendation.dto.ProblemRecommendationDTO;

public interface ProblemRecommendationService {
    List<ProblemRecommendationDTO> recommendProblems();
}

