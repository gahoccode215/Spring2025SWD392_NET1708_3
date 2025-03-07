package com.swd392.skincare_products_sales_system.dto.request.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.swd392.skincare_products_sales_system.model.product.Batch;
import com.swd392.skincare_products_sales_system.model.product.Specification;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductCreationRequest {
    @NotBlank(message = "product name can not blank")
    String name;
    Double price;
    String description;
    String ingredient;
    String usageInstruction;
    String thumbnail;
    SpecificationCreationRequest specification;
    List<BatchCreationRequest> batches;
    Long brand_id;
    String category_id;
}
