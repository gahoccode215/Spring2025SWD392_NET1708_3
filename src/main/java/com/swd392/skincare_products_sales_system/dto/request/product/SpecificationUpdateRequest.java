package com.swd392.skincare_products_sales_system.dto.request.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpecificationUpdateRequest {
    String origin;
    String brandOrigin;
    String manufacturingLocation;
    String skinType;
}
