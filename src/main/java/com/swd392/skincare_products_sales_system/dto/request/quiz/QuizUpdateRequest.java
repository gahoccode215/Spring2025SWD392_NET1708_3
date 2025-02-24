package com.swd392.skincare_products_sales_system.dto.request.quiz;


import com.swd392.skincare_products_sales_system.enums.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuizUpdateRequest {
    @NotBlank(message = "Quiz description is required")
    private String description;
    @NotBlank(message = "Quiz title is required")
    private String title;
    List<QuestionRequest> questions;
    Status status;
    Long id;
}
