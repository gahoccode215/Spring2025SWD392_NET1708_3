package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.CategoryCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.CategoryUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.CategoryPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.CategoryResponse;
import com.swd392.skincare_products_sales_system.enums.Status;

import java.io.IOException;

public interface CategoryService {
    CategoryResponse createCategory(CategoryCreationRequest request);

    CategoryResponse updateCategory(CategoryUpdateRequest request, String id) ;

    void deleteCategory(String id);

    CategoryResponse getCategoryById(String id);

    CategoryPageResponse getCategories(boolean admin, String keyword ,int page, int size, String sortBy, String order);

    void changeCategoryStatus(String categoryId, Status status);
}
