package com.swd392.skincare_products_sales_system.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnswerResponse {
    private Long answerId;
    private String answerText;
}