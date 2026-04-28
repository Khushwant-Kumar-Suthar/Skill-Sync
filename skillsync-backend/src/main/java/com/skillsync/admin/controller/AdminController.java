package com.skillsync.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.skillsync.admin.dto.AdminStatsDTO;
import com.skillsync.admin.dto.AdminUserDTO;
import com.skillsync.admin.service.AdminService;
import com.skillsync.common.response.ApiResponse;
import com.skillsync.common.util.ResponseUtil;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * GET /api/admin/users?page=0&size=10&sortBy=name
     * Paginated list of all users with their stats.
     */
    @GetMapping("/users")
    public ApiResponse<Page<AdminUserDTO>> getAllUsers(
            @RequestParam(defaultValue = "0")    int    page,
            @RequestParam(defaultValue = "10")   int    size,
            @RequestParam(defaultValue = "name") String sortBy) {

        return ResponseUtil.success(
                "Users fetched successfully",
                adminService.getAllUsers(page, size, sortBy));
    }

    /**
     * GET /api/admin/stats
     * Platform-wide aggregate statistics.
     */
    @GetMapping("/stats")
    public ApiResponse<AdminStatsDTO> getPlatformStats() {
        return ResponseUtil.success(
                "Platform stats fetched successfully",
                adminService.getPlatformStats());
    }

    /**
     * PATCH /api/admin/users/{userId}/promote
     * Promote a USER to ADMIN role.
     */
    @PatchMapping("/users/{userId}/promote")
    public ApiResponse<Void> promoteToAdmin(@PathVariable Long userId) {
        adminService.promoteToAdmin(userId);
        return ResponseUtil.successMessage("User promoted to admin successfully");
    }

    /**
     * DELETE /api/admin/users/{userId}
     * Delete a user and all their data.
     */
    @DeleteMapping("/users/{userId}")
    public ApiResponse<Void> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseUtil.successMessage("User deleted successfully");
    }
}
