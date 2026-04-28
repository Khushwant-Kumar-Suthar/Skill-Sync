package com.skillsync.skill.dto;

import com.skillsync.common.constant.Difficulty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillRequestDTO {

	@NotBlank
	private String name;

	@NotNull
	private Long categoryId;

	@NotNull
	private Difficulty difficulty;

	private String description;
}