package com.swd392.skincare_products_sales_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopSellingProductResponse {
    private String productName;
    private Long sellingQuantity;
}
