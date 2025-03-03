package com.swd392.skincare_products_sales_system.dto.request.routine;

import com.swd392.skincare_products_sales_system.enums.TimeOfDayStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StepRequest {
     Integer stepNumber;
     @Enumerated(EnumType.STRING)
     TimeOfDayStatus timeOfDay;
     String action;
     String description;
     String note;
     String productId;
}
