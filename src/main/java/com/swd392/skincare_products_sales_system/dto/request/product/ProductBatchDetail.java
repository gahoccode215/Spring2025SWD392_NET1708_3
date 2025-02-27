package com.swd392.skincare_products_sales_system.dto.request.product;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class ProductBatchDetail {
    private String productId;
    private Integer quantity;
    private Double importPrice;
    private LocalDate manufactureDate;
    private LocalDate expirationDate;
}
