package com.skillsync.recommendation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillsync.common.response.ApiResponse;
import com.skillsync.common.util.ResponseUtil;
import com.skillsync.recommendation.dto.ProblemRecommendationDTO;
import com.skillsync.recommendation.dto.RecommendationDTO;
import com.skillsync.recommendation.service.ProblemRecommendationService;
import com.skillsync.recommendation.service.RecommendationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final ProblemRecommendationService problemRecommendationService;

    /**
     * GET /api/recommendations
     * Returns a prioritised list of skill recommendations for the logged-in user.
     */
    @GetMapping
    public ApiResponse<List<RecommendationDTO>> getRecommendations() {
        List<RecommendationDTO> recommendations =
                recommendationService.getRecommendations();
        return ResponseUtil.success("Recommendations fetched successfully",
                recommendations);
    }

    /**
     * GET /api/recommendations/problems
     * Returns a prioritised list of problem recommendations for the logged-in user.
     */
    @GetMapping("/problems")
    public ApiResponse<List<ProblemRecommendationDTO>> getProblemRecommendations() {
        return ResponseUtil.success("Problem recommendations fetched successfully",
                problemRecommendationService.recommendProblems());
    }
}
