package com.skillsync.dsa.plan.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillsync.common.response.ApiResponse;
import com.skillsync.common.util.ResponseUtil;
import com.skillsync.dsa.plan.dto.DsaPlanDTO;
import com.skillsync.dsa.plan.dto.PlanProgressDTO;
import com.skillsync.dsa.plan.service.DsaPlanService;
import com.skillsync.dsa.problem.dto.ProblemDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dsa/plans")
@RequiredArgsConstructor
public class DsaPlanController {

    private final DsaPlanService planService;

    @GetMapping
    public ApiResponse<List<DsaPlanDTO>> listPlans() {
        return ResponseUtil.success("Plans fetched successfully",
                planService.listPlans());
    }

    @GetMapping("/{planSlug}/problems")
    public ApiResponse<List<ProblemDTO>> listPlanProblems(@PathVariable String planSlug) {
        return ResponseUtil.success("Plan problems fetched successfully",
                planService.listPlanProblems(planSlug));
    }

    @GetMapping("/{planSlug}/progress")
    public ApiResponse<PlanProgressDTO> getMyPlanProgress(@PathVariable String planSlug) {
        return ResponseUtil.success("Plan progress fetched successfully",
                planService.getMyPlanProgress(planSlug));
    }
}

