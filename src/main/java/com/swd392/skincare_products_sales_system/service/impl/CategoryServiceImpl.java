package com.swd392.skincare_products_sales_system.service.impl;

import com.github.slugify.Slugify;
import com.swd392.skincare_products_sales_system.dto.request.CategoryCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.CategoryUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.CategoryResponse;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.mapper.CategoryMapper;
import com.swd392.skincare_products_sales_system.model.Category;
import com.swd392.skincare_products_sales_system.repository.CategoryRepository;
import com.swd392.skincare_products_sales_system.service.CategoryService;
import com.swd392.skincare_products_sales_system.util.SlugUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    Slugify slugify;
    CategoryMapper categoryMapper;
    SlugUtil slugUtil;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public CategoryResponse createCategory(CategoryCreationRequest request) {
        Category category = categoryMapper.toCategory(request);
        category.setSlug(generateUniqueSlug(category.getName()));
        categoryRepository.save(category);
        log.info("{}", category);
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CategoryResponse updateCategory(CategoryUpdateRequest request, String categoryId) {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        categoryMapper.updateCategory(category, request);
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public CategoryResponse getCategory(String categoryId) {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        return categoryMapper.toCategoryResponse(category);
    }

    // Generate a unique slug
    private String generateUniqueSlug(String name) {
        String baseSlug = slugify.slugify(name);
        String uniqueSlug = baseSlug;

        while (categoryRepository.existsBySlug(uniqueSlug)) {
            uniqueSlug = baseSlug + "-" + slugUtil.generateRandomString(6);
        }
        return uniqueSlug;
    }
}
