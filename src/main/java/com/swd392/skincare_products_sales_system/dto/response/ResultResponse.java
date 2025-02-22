package com.swd392.skincare_products_sales_system.dto.response;

import com.swd392.skincare_products_sales_system.enums.SkinType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResultResponse {
    String result;
}
