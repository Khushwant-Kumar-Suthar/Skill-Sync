package com.skillsync.dsa.problem.dto;

import java.util.Set;

import com.skillsync.dsa.common.Difficulty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProblemRequest {

    @NotBlank
    private String title;

    private String slug;

    @NotNull
    private Difficulty difficulty;

    private String description;

    private String sourceUrl;

    private Set<String> tags;
}
