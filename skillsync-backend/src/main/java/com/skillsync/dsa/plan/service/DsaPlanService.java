package com.skillsync.dsa.plan.service;

import java.util.List;

import com.skillsync.dsa.plan.dto.DsaPlanDTO;
import com.skillsync.dsa.plan.dto.PlanProgressDTO;
import com.skillsync.dsa.problem.dto.ProblemDTO;

public interface DsaPlanService {
    List<DsaPlanDTO> listPlans();
    List<ProblemDTO> listPlanProblems(String planSlug);
    PlanProgressDTO getMyPlanProgress(String planSlug);
}

