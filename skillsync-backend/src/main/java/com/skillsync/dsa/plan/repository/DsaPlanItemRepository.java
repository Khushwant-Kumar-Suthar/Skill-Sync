package com.skillsync.dsa.plan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.skillsync.dsa.plan.entity.DsaPlan;
import com.skillsync.dsa.plan.entity.DsaPlanItem;

public interface DsaPlanItemRepository extends JpaRepository<DsaPlanItem, Long> {

    long countByPlan(DsaPlan plan);

    @Query("""
            select pi
            from DsaPlanItem pi
            join fetch pi.problem p
            left join fetch p.tags
            where pi.plan = :plan
            order by pi.orderIndex asc
            """)
    List<DsaPlanItem> findItemsWithProblemAndTagsByPlan(@Param("plan") DsaPlan plan);
}

