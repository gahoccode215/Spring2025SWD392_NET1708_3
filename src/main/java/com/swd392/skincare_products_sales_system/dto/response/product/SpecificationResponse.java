package com.swd392.skincare_products_sales_system.dto.response.product;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpecificationResponse {
    String origin;
    String brandOrigin;
    String manufacturingLocation;
    String skinType;
}
