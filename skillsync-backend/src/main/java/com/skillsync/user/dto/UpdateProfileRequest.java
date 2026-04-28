package com.skillsync.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
public class UpdateProfileRequest {

    @NotBlank(message = "Name cannot be blank")
    private String name;
}