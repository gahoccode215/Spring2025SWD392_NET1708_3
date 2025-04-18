package com.swd392.skincare_products_sales_system.dto.response.order;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemResponse {
    Long id;
    String productId;
    String productName;
    Integer quantity;
    Double price;
    Double totalPrice;
    String thumbnailProduct;
}
