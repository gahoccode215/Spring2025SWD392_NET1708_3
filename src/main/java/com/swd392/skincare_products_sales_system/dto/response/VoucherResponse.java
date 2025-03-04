package com.swd392.skincare_products_sales_system.dto.response;

import com.swd392.skincare_products_sales_system.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherResponse {
    String voucherName;
    String voucherCode;
    Integer point;
    LocalDate startDate;
    LocalDate endDate;
    String description;
    Float discountAmount;
    Long id;
    Status status;
    Integer quantity;
}
