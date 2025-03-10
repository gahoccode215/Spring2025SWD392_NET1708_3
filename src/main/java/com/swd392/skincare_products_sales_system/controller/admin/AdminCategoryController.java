package com.swd392.skincare_products_sales_system.controller.admin;

import com.swd392.skincare_products_sales_system.dto.request.product.CategoryCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.product.CategoryUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.ApiResponse;
import com.swd392.skincare_products_sales_system.dto.response.product.CategoryPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.product.CategoryResponse;
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



@RestController
@RequestMapping("/admin/categories")
@Tag(name = "Category Controller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCategoryController {
    CategoryService categoryService;

    @PostMapping
    @Operation(summary = "Tạo danh mục (ADMIN, MANAGER)", description = "API Tạo danh mục")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CategoryResponse> createCategory(@RequestBody @Valid CategoryCreationRequest request
    )  {
        return ApiResponse.<CategoryResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Tạo danh mục thành công")
                .result(categoryService.createCategory(request))
                .build();
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "Cập nhật danh mục (ADMIN, MANAGER)", description = "API Cập nhật danh mục")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse<CategoryResponse> updateCategory(@RequestBody @Valid CategoryUpdateRequest request, @PathVariable String categoryId) {
        return ApiResponse.<CategoryResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Cập nhật danh mục thành công")
                .result(categoryService.updateCategory(request, categoryId))
                .build();
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Xóa danh mục (ADMIN, MANAGER)", description = "API xóa danh mục bằng Id")
    public ApiResponse<Void> deleteCategory(@PathVariable String categoryId) {
        categoryService.deleteCategory(categoryId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Xóa danh mục thành công")
                .build();
    }

    @GetMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Lấy chi tiết danh mục (ADMIN, MANAGER, STAFF)", description = "API Lấy chi tiết danh mục ")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ApiResponse<CategoryResponse> getCategory(@PathVariable String categoryId) {
        return ApiResponse.<CategoryResponse>builder()
                .code(HttpStatus.OK.value())
                .message("get category detail successfully")
                .result(categoryService.getCategoryById(categoryId))
                .build();
    }


    @GetMapping
    @Operation(summary = "Lấy danh sách danh mục (ADMIN, MANAGER, STAFF)", description = "Lấy danh sách danh mục với phân trang, search, sort")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    public ApiResponse<CategoryPageResponse> getAllCategories(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "100") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order) {
        return ApiResponse.<CategoryPageResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy danh sách danh mục thành công")
                .result(categoryService.getCategories(keyword, page, size, sortBy, order))
                .build();
    }
}
