package com.swd392.skincare_products_sales_system.dto.response.cart;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponse {
    Long cartId;
    String username;
    List<CartItemResponse> items;
    Double totalPrice;
}
