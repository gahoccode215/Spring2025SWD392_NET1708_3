package com.swd392.skincare_products_sales_system.dto.response.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swd392.skincare_products_sales_system.dto.response.user.UserResponse;
import com.swd392.skincare_products_sales_system.model.product.Product;
import com.swd392.skincare_products_sales_system.model.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedBackResponse {
    Long id;

    UserResponse userResponse;

    String description;

    Integer rating;
}
