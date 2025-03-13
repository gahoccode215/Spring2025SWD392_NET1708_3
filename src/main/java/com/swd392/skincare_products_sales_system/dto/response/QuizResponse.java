package com.swd392.skincare_products_sales_system.dto.response;

import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.model.SkincareService;
import com.swd392.skincare_products_sales_system.model.quiz.Answer;
import com.swd392.skincare_products_sales_system.model.quiz.Question;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuizResponse {
    Long id;
    String description;
    String title;
    Status status;
    List<QuestionResponse> question;

}
