package com.skillsync.skill.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillsync.common.exception.ResourceNotFoundException;
import com.skillsync.skill.category.entity.SkillCategory;
import com.skillsync.skill.category.repository.SkillCategoryRepository;
import com.skillsync.skill.dto.SkillRequestDTO;
import com.skillsync.skill.dto.SkillResponseDTO;
import com.skillsync.skill.entity.Skill;
import com.skillsync.skill.repository.SkillRepository;
import com.skillsync.skill.service.SkillService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

	private static final Logger log = LoggerFactory.getLogger(SkillServiceImpl.class);
	private final SkillRepository skillRepository;
	private final SkillCategoryRepository categoryRepository;

	@Override
	public void createSkill(SkillRequestDTO request) {

		SkillCategory category = categoryRepository.findById(request.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException("Category not found", "CATEGORY_NOT_FOUND"));

		Skill skill = Skill.builder().name(request.getName()).category(category).difficulty(request.getDifficulty())
				.description(request.getDescription()).build();

		skillRepository.save(skill);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<SkillResponseDTO> getAllSkills(String keyword, int page, int size, String sortBy) {

	    log.info("Fetching skills | keyword: {} | page: {} | size: {} | sortBy: {}", keyword, page, size, sortBy);

	    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());

	    Page<Skill> skills;

	    if (keyword != null && !keyword.isEmpty()) {
	        skills = skillRepository.findByNameContainingIgnoreCase(keyword, pageable);
	    } else {
	        skills = skillRepository.findAll(pageable);
	    }

	    return skills.map(skill -> SkillResponseDTO.builder()
	            .id(skill.getId())
	            .name(skill.getName())
	            .categoryName(
	                skill.getCategory() != null ? skill.getCategory().getName() : null
	            )
	            .build());
	}
}