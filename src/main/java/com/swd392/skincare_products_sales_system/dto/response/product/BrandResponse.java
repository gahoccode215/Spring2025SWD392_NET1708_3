package com.swd392.skincare_products_sales_system.dto.response.product;

import com.swd392.skincare_products_sales_system.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandResponse {
    Long id;
    String name;
    String description;
    String thumbnail;
    String slug;
    Status status;
}
