package com.skillsync.dsa.attempt.repository;

import com.skillsync.dsa.attempt.entity.Attempt;
import com.skillsync.dsa.common.AttemptStatus;
import com.skillsync.dsa.plan.entity.DsaPlan;
import com.skillsync.dsa.problem.entity.Problem;
import com.skillsync.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {

    Optional<Attempt> findTopByUserAndProblemOrderByAttemptedAtDesc(
            User user,
            Problem problem
    );

    List<Attempt> findByUserOrderByAttemptedAtDesc(User user);

    List<Attempt> findByUserAndStatusOrderByAttemptedAtDesc(
            User user,
            AttemptStatus status
    );

    @Query("""
            select count(distinct pi.problem.id)
            from com.skillsync.dsa.plan.entity.DsaPlanItem pi
            join com.skillsync.dsa.attempt.entity.Attempt a
              on a.problem = pi.problem and a.user = :user and a.status = :status
            where pi.plan = :plan
            """)
    long countDistinctProblemsInPlanByUserAndStatus(
            @Param("plan") DsaPlan plan,
            @Param("user") User user,
            @Param("status") AttemptStatus status
    );
}

