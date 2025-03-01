package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.product.CategoryCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.product.CategoryUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.product.CategoryPageResponse;
import com.swd392.skincare_products_sales_system.dto.response.product.CategoryResponse;
import com.swd392.skincare_products_sales_system.enums.Status;

public interface CategoryService {
    CategoryResponse createCategory(CategoryCreationRequest request);

    CategoryResponse updateCategory(CategoryUpdateRequest request, String id) ;

    void deleteCategory(String id);

    CategoryResponse getCategoryById(String id);

    CategoryPageResponse getCategories(boolean admin, String keyword ,int page, int size, String sortBy, String order);

    void changeCategoryStatus(String categoryId, Status status);
}
