package com.skillsync.recommendation.service;

import java.util.List;

import com.skillsync.recommendation.dto.RecommendationDTO;

public interface RecommendationService {
    List<RecommendationDTO> getRecommendations();
}
