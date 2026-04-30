package com.skillsync.dsa.problem.service;

import com.skillsync.dsa.common.Difficulty;
import com.skillsync.dsa.problem.dto.ProblemDTO;

import java.util.List;

public interface ProblemService {
    List<ProblemDTO> listProblems(Difficulty difficulty, String tag);
    ProblemDTO getProblemBySlug(String slug);
}

