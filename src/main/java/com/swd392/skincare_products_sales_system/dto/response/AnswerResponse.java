package com.swd392.skincare_products_sales_system.dto.response;

import com.swd392.skincare_products_sales_system.enums.SkinType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnswerResponse {
    private Long answerId;
    private String answerText;
    @Enumerated(EnumType.STRING)
    SkinType skinType;
    Boolean isDeleted;
}