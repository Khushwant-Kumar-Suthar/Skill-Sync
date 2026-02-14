package com.skillsync.auth.service.impl;

import org.springframework.stereotype.Service;

import com.skillsync.auth.dto.request.RegisterRequestDto;
import com.skillsync.auth.dto.response.UserResponseDto;
import com.skillsync.auth.repository.RoleRepository;
import com.skillsync.auth.repository.UserRepository;
import com.skillsync.auth.service.AuthService;
@Service
public class AuthServiceImpl implements AuthService {
     
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	
	public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
		  this.userRepository = userRepository;
		  this.roleRepository = roleRepository;
	}
	
	
	public UserResponseDto register(RegisterRequestDto request) {
		//logic will be written later
		return null;
	}
}
