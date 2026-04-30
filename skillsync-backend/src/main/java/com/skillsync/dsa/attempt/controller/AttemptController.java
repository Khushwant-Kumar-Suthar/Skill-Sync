package com.skillsync.dsa.attempt.controller;

import com.skillsync.common.response.ApiResponse;
import com.skillsync.common.util.ResponseUtil;
import com.skillsync.dsa.attempt.dto.AttemptDTO;
import com.skillsync.dsa.attempt.dto.CreateAttemptRequest;
import com.skillsync.dsa.attempt.service.AttemptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

