package com.skillsync.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private Long   id;
    private String name;
    private String email;
    private String role;
}