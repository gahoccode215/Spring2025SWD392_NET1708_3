package com.swd392.skincare_products_sales_system.dto.request.routine;

import com.swd392.skincare_products_sales_system.enums.RoutineStatusEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoutineUpdateRequest {
    Long id;
    String description;
    String routineName;
    RoutineStatusEnum routineStauts;
    LocalDateTime startDate;
    LocalDateTime endDate;
}
