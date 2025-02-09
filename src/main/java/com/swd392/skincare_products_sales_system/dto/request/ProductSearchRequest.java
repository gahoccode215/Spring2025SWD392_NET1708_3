package com.swd392.skincare_products_sales_system.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSearchRequest {
    String keyword;
    String brand;
    String skinType;
    String category;
    Double minPrice;
    Double maxPrice;
    Integer pageNumber = 0;
    Integer pageSize = 10;
    String sortBy = "name";
    String sortDirection = "ASC";
}
