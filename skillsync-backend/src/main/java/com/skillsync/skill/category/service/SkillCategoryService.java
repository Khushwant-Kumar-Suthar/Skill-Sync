package com.skillsync.skill.category.service;
import java.util.List;

import com.skillsync.skill.category.dto.SkillCategoryRequest;
import com.skillsync.skill.category.dto.SkillCategoryResponse;
public interface SkillCategoryService {
    SkillCategoryResponse createCategory(SkillCategoryRequest request);
    List<SkillCategoryResponse> getCategoriesWithSkills();
}