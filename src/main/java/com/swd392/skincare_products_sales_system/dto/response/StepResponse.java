package com.swd392.skincare_products_sales_system.dto.response;


import com.swd392.skincare_products_sales_system.enums.TimeOfDayStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StepResponse {
     Long id;
     Integer stepNumber;
     @Enumerated(EnumType.STRING)
     TimeOfDayStatus timeOfDay;
     String action;
     String description;
     String productId;
}
