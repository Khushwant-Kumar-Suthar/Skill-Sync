package com.skillsync.dsa.tag.repository;

import com.skillsync.dsa.common.TagType;
import com.skillsync.dsa.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTypeAndNameIgnoreCase(TagType type, String name);
}

