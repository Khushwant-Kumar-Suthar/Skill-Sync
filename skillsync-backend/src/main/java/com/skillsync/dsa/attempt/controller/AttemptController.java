package com.skillsync.dsa.attempt.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillsync.common.response.ApiResponse;
import com.skillsync.common.util.ResponseUtil;
import com.skillsync.dsa.attempt.dto.AttemptDTO;
import com.skillsync.dsa.attempt.dto.CreateAttemptRequest;
import com.skillsync.dsa.attempt.service.AttemptService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/attempts")
@RequiredArgsConstructor
public class AttemptController {

    private final AttemptService attemptService;

    @GetMapping("/me")
    public ApiResponse<List<AttemptDTO>> listMyAttempts() {
        return ResponseUtil.success("Attempts fetched successfully",
                attemptService.listMyAttempts());
    }

    @PostMapping("/problems/{problemId}")
    public ApiResponse<AttemptDTO> createAttempt(
            @PathVariable Long problemId,
            @Valid @RequestBody CreateAttemptRequest request
    ) {
        return ResponseUtil.success("Attempt created successfully",
                attemptService.createAttempt(problemId, request));
    }
}

