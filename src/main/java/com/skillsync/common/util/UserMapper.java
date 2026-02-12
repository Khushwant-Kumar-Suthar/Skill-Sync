package com.skillsync.common.util;

import com.skillsync.auth.dto.request.RegisterRequestDto;
import com.skillsync.auth.dto.response.UserResponseDto;
import com.skillsync.auth.entity.Role;
import com.skillsync.auth.entity.User;

public class UserMapper {

	private UserMapper() {
	}

	public static User toEntity(RegisterRequestDto dto, Role role) {
		User user = new User();
		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());
		user.setRole(role);
		return user;
	}

	public static UserResponseDto toDto(User user) {
		return new UserResponseDto(user.getId(), user.getName(), user.getEmail(), user.getRole().getName());
	}
}
