package com.skillsync.skill.progress.service;


import java.util.List;

import com.skillsync.skill.progress.dto.ProgressResponse;

public interface ProgressService {

    List<ProgressResponse> getUserProgress();
}
