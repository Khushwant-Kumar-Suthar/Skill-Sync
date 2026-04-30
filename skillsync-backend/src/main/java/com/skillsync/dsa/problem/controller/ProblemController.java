package com.skillsync.dsa.problem.controller;

import com.skillsync.common.response.ApiResponse;
import com.skillsync.common.util.ResponseUtil;
import com.skillsync.dsa.common.Difficulty;
import com.skillsync.dsa.problem.dto.ProblemDTO;
import com.skillsync.dsa.problem.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping
    public ApiResponse<List<ProblemDTO>> listProblems(
            @RequestParam(required = false) Difficulty difficulty,
            @RequestParam(required = false) String tag
    ) {
        return ResponseUtil.success("Problems fetched successfully",
                problemService.listProblems(difficulty, tag));
    }

    @GetMapping("/{slug}")
    public ApiResponse<ProblemDTO> getBySlug(@PathVariable String slug) {
        return ResponseUtil.success("Problem fetched successfully",
                problemService.getProblemBySlug(slug));
    }
}

