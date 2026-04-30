package com.skillsync.dsa.problem.dto;

import com.skillsync.dsa.common.Difficulty;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class ProblemDTO {
    private Long id;
    private String title;
    private String slug;
    private Difficulty difficulty;
    private String sourceUrl;
    private Set<String> tags;
}

