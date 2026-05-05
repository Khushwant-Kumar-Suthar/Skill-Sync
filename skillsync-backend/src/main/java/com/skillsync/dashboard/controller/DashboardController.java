package com.skillsync.dashboard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillsync.common.response.ApiResponse;
import com.skillsync.common.util.ResponseUtil;
import com.skillsync.dashboard.dto.DashboardResponse;
import com.skillsync.dashboard.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * GET /api/dashboard
     * Returns a full analytics snapshot for the logged-in user.
     */
    @GetMapping
    public ApiResponse<DashboardResponse> getDashboard() {
        return ResponseUtil.success(
                "Dashboard fetched successfully",
                dashboardService.getDashboard());
    }
}
