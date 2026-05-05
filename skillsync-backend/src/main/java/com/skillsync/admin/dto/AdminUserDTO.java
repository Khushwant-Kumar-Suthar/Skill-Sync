package com.skillsync.admin.dto;

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
public class AdminUserDTO {

    private Long   id;
    private String name;
    private String email;
    private String role;
    private String createdAt;

    // Quick stats
    private int    skillsTracked;
    private double averageProgress;
    private int    totalActivities;
}
