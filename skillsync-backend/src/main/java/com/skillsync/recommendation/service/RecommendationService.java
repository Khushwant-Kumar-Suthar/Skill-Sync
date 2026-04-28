package com.skillsync.recommendation.service;

import com.skillsync.recommendation.dto.RecommendationDTO;

import java.util.List;

public interface RecommendationService {
    List<RecommendationDTO> getRecommendations();
}
