package com.swd392.skincare_products_sales_system.dto.response.product;

import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.entity.product.Brand;
import com.swd392.skincare_products_sales_system.entity.product.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

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
    String ingredient;
    String usageInstruction;
    Double rating;
    LocalDate expirationTime;
    SpecificationResponse specification;
    Integer stock;
    String slug;
    Status status;
    Category category;
    Brand brand;
    List<FeedBackResponse> feedBacks;
}
