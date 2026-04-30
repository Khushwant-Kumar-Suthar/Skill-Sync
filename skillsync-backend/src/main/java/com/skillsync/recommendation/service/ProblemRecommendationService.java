package com.skillsync.recommendation.service;

import com.skillsync.recommendation.dto.ProblemRecommendationDTO;

import java.util.List;

public interface ProblemRecommendationService {
    List<ProblemRecommendationDTO> recommendProblems();
}

