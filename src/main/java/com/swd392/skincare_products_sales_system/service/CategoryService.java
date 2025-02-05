package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.CategoryCreationRequest;
import com.swd392.skincare_products_sales_system.dto.response.CategoryResponse;

public interface CategoryService {
    CategoryResponse createCategory(CategoryCreationRequest request);
}
