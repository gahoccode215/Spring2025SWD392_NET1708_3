package com.swd392.skincare_products_sales_system.dto.response.product;

import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.model.product.Brand;
import com.swd392.skincare_products_sales_system.model.product.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String id;
    String name;
    Double price;
    String description;
    String thumbnail;
    Integer stock;
    String slug;
    Status status;
//    String category_id;
//    String brand_id;
    Category category;
    Brand brand;
}
