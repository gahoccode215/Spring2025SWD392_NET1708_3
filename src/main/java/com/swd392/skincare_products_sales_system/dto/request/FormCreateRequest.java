package com.swd392.skincare_products_sales_system.dto.request;

import com.swd392.skincare_products_sales_system.enums.SkinType;
import com.swd392.skincare_products_sales_system.model.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FormCreateRequest {
    Long skincareServiceId;
    SkinType skinType;
    String note;
    String skinCondition;
    String image;
    String allergy;
    LocalDate bookDate;
    String expertId;


}
