package com.skillsync.skill.category.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillsync.common.response.ApiResponse;
import com.skillsync.common.util.ResponseUtil;
import com.skillsync.skill.category.dto.SkillCategoryRequest;
import com.skillsync.skill.category.dto.SkillCategoryResponse;
import com.skillsync.skill.category.service.SkillCategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class SkillCategoryController {

    private final SkillCategoryService categoryService;

    // FIX: @Valid added — without it, @NotBlank on SkillCategoryRequest is ignored.
    @PostMapping("/createCategory")
    public ApiResponse<SkillCategoryResponse> createCategory(
            @Valid @RequestBody SkillCategoryRequest request) {

        return ResponseUtil.success(
                "Category created successfully",
                categoryService.createCategory(request));
    }

    // FIX: removed the duplicate /getAllCategories endpoint — it called the exact
    // same service method as /with-skills, causing confusion and dead code.
    // Keeping only /with-skills as the canonical endpoint.
    @GetMapping("/with-skills")
    public ApiResponse<List<SkillCategoryResponse>> getCategoriesWithSkills() {

        return ResponseUtil.success(
                "Categories fetched successfully",
                categoryService.getCategoriesWithSkills());
    }
}