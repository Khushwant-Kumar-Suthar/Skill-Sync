package com.skillsync.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {
  
	@NotBlank(message = "Name is required")
	private String name;
	
	@Email(message = "Invalid email")
	@NotBlank
	private String email;
	
	@NotBlank
	@Size(min=6, message = "Password must at least 6 character")
	private String password;
}
