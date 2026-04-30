package com.skillsync.dsa.plan.repository;

import com.skillsync.dsa.plan.entity.DsaPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DsaPlanRepository extends JpaRepository<DsaPlan, Long> {
    Optional<DsaPlan> findBySlug(String slug);
}

