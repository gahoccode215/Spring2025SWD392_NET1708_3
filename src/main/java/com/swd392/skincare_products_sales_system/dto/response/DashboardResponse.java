package com.swd392.skincare_products_sales_system.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DashboardResponse {
    Long totalCustomer;
    Double totalRevenue;
    Long totalOrder;
    Long totalQuantitySold;
    List<TopSellingProductResponse> topSellingProduct;
}



