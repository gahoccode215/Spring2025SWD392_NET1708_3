package com.swd392.skincare_products_sales_system.mapper;

import com.swd392.skincare_products_sales_system.dto.request.CategoryCreationRequest;
import com.swd392.skincare_products_sales_system.dto.request.CategoryUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.CategoryResponse;
import com.swd392.skincare_products_sales_system.dto.response.QuizResponse;
import com.swd392.skincare_products_sales_system.model.Category;
import com.swd392.skincare_products_sales_system.model.Quiz;
import org.mapstruct.MappingTarget;

public interface QuizMapper {
    Quiz toQuiz(CategoryCreationRequest request);
    QuizResponse toQuizResponse(Quiz quiz);
//    void updateQuiz(@MappingTarget Category product, CategoryUpdateRequest request);
}
