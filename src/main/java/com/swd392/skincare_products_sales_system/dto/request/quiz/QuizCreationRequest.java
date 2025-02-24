package com.swd392.skincare_products_sales_system.dto.request.quiz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuizCreationRequest {
    @NotBlank(message = "Quiz description is required")
    private String description;
    @NotBlank(message = "Quiz title is required")
    private String title;
    List<QuestionRequest> questions;
}
