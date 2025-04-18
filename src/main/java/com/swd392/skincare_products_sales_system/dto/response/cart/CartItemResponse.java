package com.swd392.skincare_products_sales_system.dto.response.cart;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    String productId;
    String productName;
    String thumbnail;
    Double price;
    int quantity;
    Double totalItemPrice;
}
