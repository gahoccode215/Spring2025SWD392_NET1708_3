package com.swd392.skincare_products_sales_system.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RevenueByTime {
    private LocalDate revenueDate;
    private Double revenue;
}
