package com.skillsync.dsa.tag.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillsync.dsa.common.TagType;
import com.skillsync.dsa.tag.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTypeAndNameIgnoreCase(TagType type, String name);
}

