package com.skillsync.dsa.plan.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillsync.dsa.plan.entity.DsaPlan;

public interface DsaPlanRepository extends JpaRepository<DsaPlan, Long> {
    Optional<DsaPlan> findBySlug(String slug);
}

