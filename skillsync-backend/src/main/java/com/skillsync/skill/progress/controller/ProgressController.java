package com.skillsync.skill.progress.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillsync.common.response.ApiResponse;
import com.skillsync.common.util.ResponseUtil;
import com.skillsync.skill.progress.dto.ProgressResponse;
import com.skillsync.skill.progress.service.ProgressService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    /**
     * GET /api/progress/me
     *
     * Returns all skill progress entries for the authenticated user.
     * Endpoint renamed from /getProgress to /me — more RESTful and consistent
     * with /api/user/profile pattern used elsewhere.
     * Old path /getProgress is kept as alias for backwards compatibility.
     */
    @GetMapping({"/me", "/getProgress"})
    public ApiResponse<List<ProgressResponse>> getUserProgress() {
        return ResponseUtil.success(
                "Progress fetched successfully",
                progressService.getUserProgress());
    }
}