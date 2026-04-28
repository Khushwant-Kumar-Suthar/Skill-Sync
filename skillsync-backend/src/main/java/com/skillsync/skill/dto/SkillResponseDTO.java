package com.skillsync.skill.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkillResponseDTO {

	private Long id;
	private String name;
	private String categoryName;
}
