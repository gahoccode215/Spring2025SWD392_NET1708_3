package com.swd392.skincare_products_sales_system.dto.request.quiz;

import com.swd392.skincare_products_sales_system.enums.SkinType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnswerRequest {

    private Long answerId;
    @NotBlank()
    private String answerText;
    SkinType skinType;
}
