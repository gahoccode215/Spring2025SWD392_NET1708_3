package com.swd392.skincare_products_sales_system.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
public class QuestionResponse {
    private Long questionId;
    private String title;
    private List<AnswerResponse> answers;
}