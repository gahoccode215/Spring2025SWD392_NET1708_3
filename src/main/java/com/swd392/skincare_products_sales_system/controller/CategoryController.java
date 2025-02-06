package com.swd392.skincare_products_sales_system.controller;

import com.swd392.skincare_products_sales_system.dto.request.CategoryCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.CategoryUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.request.ProductCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.ProductUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.CategoryResponse;
import com.swd392.skincare_products_sales_system.dto.response.ProductResponse;
import com.swd392.skincare_products_sales_system.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a category", description = "API retrieve category attribute to create category")
    public ApiResponse<CategoryResponse> createCategory(@RequestBody @Valid CategoryCreationRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create category successfully")
                .result(categoryService.createCategory(request))
                .build();
    }
    @PutMapping("/{categoryId}")
    @Operation(summary = "Update a category", description = "API retrieve value to change category attribute")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<CategoryResponse> updateCategory(@RequestBody @Valid CategoryUpdateRequest request, @PathVariable String categoryId){
        return ApiResponse.<CategoryResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update product successfully")
                .result(categoryService.updateCategory(request, categoryId))
                .build();
    }
    @GetMapping("/{categoryId}")
    @Operation(summary = "Get a category", description = "API retrieve id to get category")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<CategoryResponse> getCategory(@PathVariable String categoryId){
        return ApiResponse.<CategoryResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get category successfully")
                .result(categoryService.getCategory(categoryId))
                .build();
    }
}
