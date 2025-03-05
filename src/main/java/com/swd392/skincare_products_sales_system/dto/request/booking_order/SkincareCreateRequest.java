package com.swd392.skincare_products_sales_system.dto.request.booking_order;


import com.swd392.skincare_products_sales_system.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SkincareCreateRequest {
    @NotNull(message = "Service Name cannot be null")
    String serviceName;
    String description;
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be greater than or equal to 0")
    Float price;
    @Enumerated(EnumType.STRING)
    Status status;
}
