package com.skillsync.skill.service;

import org.springframework.data.domain.Page;

import com.skillsync.skill.dto.SkillRequestDTO;
import com.skillsync.skill.dto.SkillResponseDTO;


public interface SkillService {
    void createSkill(SkillRequestDTO request);
    Page<SkillResponseDTO> getAllSkills(String keyword, int page, int size, String sortBy);
  
}
