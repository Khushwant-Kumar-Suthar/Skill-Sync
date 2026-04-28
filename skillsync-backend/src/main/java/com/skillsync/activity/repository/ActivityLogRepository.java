package com.skillsync.activity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.skillsync.activity.entity.ActivityLog;
import com.skillsync.user.entity.User;

public interface ActivityLogRepository
        extends JpaRepository<ActivityLog, Long> {

    long countByUser(User user);

    // FIX: was int — SUM can exceed Integer.MAX_VALUE. Long is correct.
    // COALESCE ensures 0 is returned instead of null when no rows exist.
    @Query("SELECT COALESCE(SUM(a.timeSpentMinutes), 0) " +
           "FROM ActivityLog a WHERE a.user = :user")
    Long sumTimeSpentByUser(@Param("user") User user);

    // FIX: platform-wide sum uses DB aggregate — avoids loading all rows into memory
    @Query("SELECT COALESCE(SUM(a.timeSpentMinutes), 0) FROM ActivityLog a")
    Long sumAllTimeSpent();
}