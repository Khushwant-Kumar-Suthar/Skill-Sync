package com.skillsync.dsa.tag.entity;

import com.skillsync.common.util.BaseEntity;
import com.skillsync.dsa.common.TagType;
import jakarta.persistence.*;
import lombok.*;

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

