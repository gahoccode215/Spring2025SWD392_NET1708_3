package com.swd392.skincare_products_sales_system.dto.response.product;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BatchResponse {
    String id;
    String batchCode;
    String productId;
    Integer quantity;
    Double importPrice;
    LocalDate manufactureDate;
    LocalDate expirationDate;
}
