package com.swd392.skincare_products_sales_system.dto.request.booking_order;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AsignExpertRequest {
    String expertId;

}
