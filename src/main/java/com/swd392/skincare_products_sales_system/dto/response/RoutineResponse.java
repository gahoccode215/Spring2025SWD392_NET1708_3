package com.swd392.skincare_products_sales_system.dto.response;

import com.swd392.skincare_products_sales_system.enums.RoutineStatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoutineResponse {
    Long id;
    String description;
    String routineName;

    @Enumerated(EnumType.STRING)
    RoutineStatusEnum routineStauts;

    @FutureOrPresent
    LocalDateTime startDate;
    @FutureOrPresent
    LocalDateTime endDate;
}
