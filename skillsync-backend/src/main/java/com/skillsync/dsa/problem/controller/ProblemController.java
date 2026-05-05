package com.skillsync.dsa.problem.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skillsync.common.response.ApiResponse;
import com.skillsync.common.util.ResponseUtil;
import com.skillsync.dsa.common.Difficulty;
import com.skillsync.dsa.problem.dto.CreateProblemRequest;
import com.skillsync.dsa.problem.dto.ProblemDTO;
import com.skillsync.dsa.problem.service.ProblemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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

    @PostMapping
    public ApiResponse<ProblemDTO> createProblem(
            @Valid @RequestBody CreateProblemRequest request) {
        return ResponseUtil.success("Problem created successfully",
                problemService.createProblem(request));
    }
}

