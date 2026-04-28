package com.skillsync.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.skillsync.common.response.ApiResponse;
import com.skillsync.common.util.ResponseUtil;
import com.skillsync.user.dto.ChangePasswordRequest;
import com.skillsync.user.dto.UpdateProfileRequest;
import com.skillsync.user.dto.UserProfileDTO;
import com.skillsync.user.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * GET /api/user/profile
     * Returns the logged-in user's profile.
     */
    @GetMapping("/profile")
    public ApiResponse<UserProfileDTO> getProfile() {
        return ResponseUtil.success(
                "Profile fetched successfully",
                userService.getProfile());
    }

    /**
     * PUT /api/user/profile
     * Updates the logged-in user's display name.
     */
    @PutMapping("/profile")
    public ApiResponse<UserProfileDTO> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseUtil.success(
                "Profile updated successfully",
                userService.updateProfile(request));
    }

    /**
     * PUT /api/user/change-password
     * Changes the logged-in user's password.
     */
    @PutMapping("/change-password")
    public ApiResponse<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseUtil.successMessage("Password changed successfully");
    }
}
