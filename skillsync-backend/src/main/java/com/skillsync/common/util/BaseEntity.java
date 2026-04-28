package com.skillsync.common.util;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Base entity providing id, createdAt, updatedAt for all entities.
 *
 * NOTE: Delete the duplicate com.skillsync.common.entity.BaseEntity.
 * All entities must import from com.skillsync.common.util.BaseEntity.
 */
@MappedSuperclass
@Getter
@Setter   // Setter needed on createdAt/updatedAt for DataSeeder historical dates
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // updatable = false prevents accidental overwrites after first persist
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        // Only set if not already provided (e.g. by DataSeeder for historical dates)
        if (this.createdAt == null) this.createdAt = now;
        if (this.updatedAt == null) this.updatedAt = now;
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}