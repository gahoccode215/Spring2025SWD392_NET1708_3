package com.swd392.skincare_products_sales_system.dto.response.product;

import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.model.product.Brand;
import com.swd392.skincare_products_sales_system.model.product.Category;
import com.swd392.skincare_products_sales_system.model.product.FeedBack;
import com.swd392.skincare_products_sales_system.model.product.Specification;
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
    Specification specification;
    Integer stock;
    String slug;
    Status status;
    Category category;
    Brand brand;
    List<FeedBack> feedBacks;
}
