package com.skillsync.dsa.plan.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DsaPlanDTO {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private Integer totalProblems;
}

