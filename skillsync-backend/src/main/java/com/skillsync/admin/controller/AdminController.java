package com.skillsync.admin.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skillsync.admin.dto.AdminRoadmapDTO;
import com.skillsync.admin.dto.AdminSkillProgressDTO;
import com.skillsync.admin.dto.AdminStatsDTO;
import com.skillsync.admin.dto.AdminUserDTO;
import com.skillsync.admin.service.AdminService;
import com.skillsync.common.response.ApiResponse;
import com.skillsync.common.util.ResponseUtil;

import lombok.RequiredArgsConstructor;

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

    @GetMapping("/roadmaps")
    public ApiResponse<List<AdminRoadmapDTO>> getAllRoadmaps() {
        return ResponseUtil.success(
                "Roadmaps fetched successfully",
                adminService.getAllRoadmaps());
    }

    @GetMapping("/progress")
    public ApiResponse<List<AdminSkillProgressDTO>> getAllSkillProgress() {
        return ResponseUtil.success(
                "Skill progress fetched successfully",
                adminService.getAllSkillProgress());
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
