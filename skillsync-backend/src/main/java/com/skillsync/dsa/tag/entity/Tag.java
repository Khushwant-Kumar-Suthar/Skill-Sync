package com.skillsync.dsa.tag.entity;

import com.skillsync.common.util.BaseEntity;
import com.skillsync.dsa.common.TagType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "dsa_tags",
        uniqueConstraints = @UniqueConstraint(columnNames = {"type", "name"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TagType type;

    @Column(nullable = false)
    private String name;
}

