package com.skillsync.admin.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.skillsync.admin.dto.AdminRoadmapDTO;
import com.skillsync.admin.dto.AdminSkillProgressDTO;
import com.skillsync.admin.dto.AdminStatsDTO;
import com.skillsync.admin.dto.AdminUserDTO;

public interface AdminService {

    /** Paginated list of all users with quick stats. */
    Page<AdminUserDTO> getAllUsers(int page, int size, String sortBy);

    /** Platform-wide aggregate statistics. */
    AdminStatsDTO getPlatformStats();

    /** All generated user roadmaps grouped by user. */
    List<AdminRoadmapDTO> getAllRoadmaps();

    /** Per-user progress in each skill and skill category. */
    List<AdminSkillProgressDTO> getAllSkillProgress();

    /** Promote a user to ADMIN role. */
    void promoteToAdmin(Long userId);

    /** Delete a user and all their data. */
    void deleteUser(Long userId);
}
