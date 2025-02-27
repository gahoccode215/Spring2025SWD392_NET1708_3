package com.swd392.skincare_products_sales_system.dto.response;

import com.swd392.skincare_products_sales_system.model.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BatchResponse {
    Long id;
    Product product;
    Integer quantity;
    LocalDate manufactureDate;
    LocalDate expirationDate;
}
