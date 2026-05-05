package com.skillsync.skill.dto;

import com.skillsync.common.constant.Difficulty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkillResponseDTO {

	private Long id;
	private String name;
	private String categoryName;
	private Difficulty difficulty;
	private String description;
}
