package com.swd392.skincare_products_sales_system.dto.request.voucher;

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
    @NotNull(message = "VoucherName cannot be null")
    String voucherName;
    @NotNull(message = "VoucherCode cannot be null")
    String voucherCode;
    @DecimalMin(value = "0", inclusive = true, message = "Price must be greater than or equal to 0")
    Integer point;
    LocalDate startDate;
    LocalDate endDate;
    String description;
    @DecimalMin(value = "0.0", inclusive = true, message = "DiscountAmount must be greater than or equal to 0")
    Float discountAmount;
    @Enumerated(EnumType.STRING)
    Status status;
    @DecimalMin(value = "0", inclusive = true, message = "Quantity must be greater than or equal to 0")
    Integer quantity;
}
