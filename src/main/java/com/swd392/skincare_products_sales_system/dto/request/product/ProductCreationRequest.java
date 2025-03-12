package com.swd392.skincare_products_sales_system.dto.request.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductCreationRequest {
    String name;
    Double price;
    String description;
    String ingredient;
    String usageInstruction;
    String thumbnail;
    SpecificationCreationRequest specification;
    Long brand_id;
    String category_id;
}
