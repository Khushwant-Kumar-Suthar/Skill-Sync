package com.skillsync.skill.category.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.skillsync.skill.category.dto.SkillCategoryRequest;
import com.skillsync.skill.category.dto.SkillCategoryResponse;
import com.skillsync.skill.category.entity.SkillCategory;
import com.skillsync.skill.category.repository.SkillCategoryRepository;
import com.skillsync.skill.category.service.SkillCategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkillCategoryServiceImpl implements SkillCategoryService {

    private static final Logger logger =
            LoggerFactory.getLogger(SkillCategoryServiceImpl.class);

    private final SkillCategoryRepository categoryRepository;

    @Override
    public SkillCategoryResponse createCategory(SkillCategoryRequest request) {

        logger.info("Creating category: {}", request.getName());

        SkillCategory category = SkillCategory.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        SkillCategory saved = categoryRepository.save(category);
        logger.info("Category created with id: {}", saved.getId());

        return SkillCategoryResponse.builder()
                .categoryId(saved.getId())
                .categoryName(saved.getName())
                .build();
    }

    @Override
    public List<SkillCategoryResponse> getCategoriesWithSkills() {

        // FIX: was categoryRepository.findAll() which triggers N+1:
        // one query for categories + one query per category to load its skills.
        // Now uses a single JOIN FETCH query. See repository for the new method.
        List<SkillCategory> categories =
                categoryRepository.findAllWithSkills();

        return categories.stream()
                .map(category -> {
                    List<SkillCategoryResponse.SkillDTO> skills =
                            category.getSkills().stream()
                                    .map(skill -> SkillCategoryResponse.SkillDTO.builder()
                                            .id(skill.getId())
                                            .name(skill.getName())
                                            .build())
                                    .toList();

                    return SkillCategoryResponse.builder()
                            .categoryId(category.getId())
                            .categoryName(category.getName())
                            .skills(skills)
                            .build();
                })
                .toList();
    }
}