package com.swd392.skincare_products_sales_system.controller.admin;

import com.swd392.skincare_products_sales_system.dto.request.CategoryCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.CategoryUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.CategoryPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.CategoryResponse;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/admin/categories")
@Tag(name = "Category Controller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCategoryController {
    CategoryService categoryService;

    @PostMapping
    @Operation(summary = "Create category (ADMIN, MANAGER)", description = "API retrieve attribute to create category")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CategoryResponse> createCategory(@RequestPart("request") @Valid CategoryCreationRequest request,
                                                        @RequestPart("thumbnail") MultipartFile thumbnail) throws IOException {
        request.setThumbnail(thumbnail);
        return ApiResponse.<CategoryResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create category successfully")
                .result(categoryService.createCategory(request))
                .build();
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "Update a category (ADMIN, MANAGER)", description = "API retrieve category id to update category")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<CategoryResponse> updateCategory(@RequestPart("request") @Valid CategoryUpdateRequest request, @PathVariable String categoryId, @RequestPart("thumbnail") MultipartFile thumbnail) throws IOException{
        request.setThumbnail(thumbnail);
        return ApiResponse.<CategoryResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update category successfully")
                .result(categoryService.updateCategory(request, categoryId))
                .build();
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Delete a category (ADMIN, MANAGER)", description = "API delete category by its id")
    public ApiResponse<Void> deleteCategory(@PathVariable String categoryId) {
        categoryService.deleteCategory(categoryId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete category successfully")
                .build();
    }

    @GetMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Get a category (ADMIN, MANAGER)", description = "API get category by its id")
    public ApiResponse<CategoryResponse> getCategory(@PathVariable String categoryId) {
        return ApiResponse.<CategoryResponse>builder()
                .code(HttpStatus.OK.value())
                .message("get category detail successfully")
                .result(categoryService.getCategoryById(categoryId))
                .build();
    }

    @PatchMapping("/change-status/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Change category status (ADMIN, MANAGER)", description = "API to change category status (ACTIVE/INACTIVE)")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<Void> changeCategoryStatus(@PathVariable String categoryId, @RequestParam Status status) {
        categoryService.changeCategoryStatus(categoryId, status);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Change status successfully")
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all categories (ADMIN, MANAGER)", description = "Retrieve all brands with pagination, sorting, and filtering.")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<CategoryPageResponse> getAllCategories(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "100") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order) {
        return ApiResponse.<CategoryPageResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get categories successfully")
                .result(categoryService.getCategories(true, keyword, page, size, sortBy, order))
                .build();
    }
}
