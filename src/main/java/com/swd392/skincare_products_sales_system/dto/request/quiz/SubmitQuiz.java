package com.swd392.skincare_products_sales_system.dto.request.quiz;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubmitQuiz {
    Map<Long, Long> answers;
}
