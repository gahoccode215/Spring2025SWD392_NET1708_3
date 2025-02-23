package com.swd392.skincare_products_sales_system.dto.response;

import com.swd392.skincare_products_sales_system.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OriginResponse {
    Long id;
    String name;
    String description;
    String thumbnail;
    String slug;
    Status status;
}
