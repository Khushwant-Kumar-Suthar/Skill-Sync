package com.skillsync.skill.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skillsync.common.response.ApiResponse;
import com.skillsync.common.util.ResponseUtil;
import com.skillsync.skill.dto.SkillRequestDTO;
import com.skillsync.skill.dto.SkillResponseDTO;
import com.skillsync.skill.service.SkillService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    // FIX: @Valid added — @NotBlank/@NotNull on SkillRequestDTO were not enforced.
    // Also removed manual ApiResponse builder in favour of ResponseUtil for consistency.
    @PostMapping("/createSkill")
    public ApiResponse<Void> createSkill(
            @Valid @RequestBody SkillRequestDTO request) {

        skillService.createSkill(request);
        return ResponseUtil.successMessage("Skill created successfully");
    }

    @GetMapping("/getAllSkills")
    public ApiResponse<Page<SkillResponseDTO>> getAllSkills(
            @RequestParam(defaultValue = "")     String keyword,
            @RequestParam(defaultValue = "0")    int    page,
            @RequestParam(defaultValue = "10")   int    size,
            @RequestParam(defaultValue = "name") String sortBy) {

        return ResponseUtil.success(
                "Skills fetched successfully",
                skillService.getAllSkills(keyword, page, size, sortBy));
    }
}