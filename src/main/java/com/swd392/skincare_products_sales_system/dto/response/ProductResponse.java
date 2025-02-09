package com.swd392.skincare_products_sales_system.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String id;
    String name;
    Double price;
    String brandName;
    String categoryName;
    String originName;
    String skinTypeType;
    Set<FeatureResponse> featureNames;
    String thumbnail;
    String slug;
}
