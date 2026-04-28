package com.skillsync.recommendation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.skillsync.common.response.ApiResponse;
import com.skillsync.common.util.ResponseUtil;
import com.skillsync.recommendation.dto.RecommendationDTO;
import com.skillsync.recommendation.service.RecommendationService;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

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
}
