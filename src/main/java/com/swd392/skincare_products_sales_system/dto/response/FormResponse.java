package com.swd392.skincare_products_sales_system.dto.response;

import com.swd392.skincare_products_sales_system.enums.BookingStatus;
import com.swd392.skincare_products_sales_system.enums.SkinType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FormResponse {
    Long skincareService;
    SkinType skinType;
    String note;
    String skinCondition;
    String image;
    String allergy;
    LocalDate bookDate;
    String userId;
    String expertName;
    Float price;
    BookingStatus status;
}
