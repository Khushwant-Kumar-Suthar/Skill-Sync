package com.skillsync.dsa.problem.service;

import java.util.List;

import com.skillsync.dsa.common.Difficulty;
import com.skillsync.dsa.problem.dto.CreateProblemRequest;
import com.skillsync.dsa.problem.dto.ProblemDTO;

public interface ProblemService {
    List<ProblemDTO> listProblems(Difficulty difficulty, String tag);
    ProblemDTO getProblemBySlug(String slug);
    ProblemDTO createProblem(CreateProblemRequest request);
}

