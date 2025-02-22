package com.swd392.skincare_products_sales_system.dto.request;

import com.swd392.skincare_products_sales_system.model.Question;
import com.swd392.skincare_products_sales_system.model.SkincareService;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuizCreationRequest {
    @NotBlank(message = "Quiz description is required")
    private String description;
    @NotBlank(message = "Quiz title is required")
    private String title;
    List<QuestionRequest> questions;
}
