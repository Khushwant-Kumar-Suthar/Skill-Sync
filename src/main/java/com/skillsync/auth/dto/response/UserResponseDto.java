package com.skillsync.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDto {
    
	private Long id;
	private String name;
	private String email;
	private String role;
	//Note -- No password ever leaves backend
}
