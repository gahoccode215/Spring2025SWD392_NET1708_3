package com.swd392.skincare_products_sales_system.dto.request.routine;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.swd392.skincare_products_sales_system.enums.RoutineStauts;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoutineCreateRequest {

    String description;
    String routineName;
    RoutineStauts routineStauts;
    LocalDateTime startDate;
    LocalDateTime endDate;
}
