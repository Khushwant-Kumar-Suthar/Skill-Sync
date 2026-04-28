package com.skillsync.admin.dto;

import lombok.*;

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
