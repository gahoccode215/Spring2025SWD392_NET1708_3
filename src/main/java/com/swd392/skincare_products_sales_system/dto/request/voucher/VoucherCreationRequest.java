package com.swd392.skincare_products_sales_system.dto.request.voucher;

import com.swd392.skincare_products_sales_system.enums.DiscountType;
import com.swd392.skincare_products_sales_system.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherCreationRequest {
    String code;
    Integer discount;
    DiscountType discountType;
    Double minOrderValue;
    String description;
    Integer point;
}
