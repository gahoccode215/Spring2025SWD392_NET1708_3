package com.swd392.skincare_products_sales_system.mapper;

import com.swd392.skincare_products_sales_system.dto.request.CategoryCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.CategoryUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.request.ProductUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.CategoryResponse;
import com.swd392.skincare_products_sales_system.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {
    Category toCategory(CategoryCreationRequest request);
    CategoryResponse toCategoryResponse(Category category);
    void updateCategory(@MappingTarget Category product, CategoryUpdateRequest request);
}
