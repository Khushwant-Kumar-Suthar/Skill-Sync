package com.skillsync.activity.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillsync.activity.dto.request.ActivityRequest;
import com.skillsync.activity.service.ActivityService;
import com.skillsync.common.response.ApiResponse;
import com.skillsync.common.util.ResponseUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    // FIX: @Valid added — without it @NotNull/@Min/@Max on ActivityRequest
    // are never evaluated. Invalid requests reach the service and cause
    // unexpected NullPointerExceptions or corrupt progress data.
    @PostMapping("/logBook")
    public ApiResponse<Void> logActivity(
            @Valid @RequestBody ActivityRequest request) {

        activityService.logActivity(
                request.getSkillId(),
                request.getTimeSpentMinutes());

        return ResponseUtil.successMessage("Activity logged successfully");
    }
}