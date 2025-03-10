package com.swd392.skincare_products_sales_system.dto.response.product;

import com.swd392.skincare_products_sales_system.entity.product.Product;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BatchResponse {
    String id;
    String batchCode;
    Product product;
    Integer quantity;
    LocalDate manufactureDate;
    LocalDate expirationDate;
}
