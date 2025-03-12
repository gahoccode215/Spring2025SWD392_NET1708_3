package com.swd392.skincare_products_sales_system.dto.request.routine;

import com.swd392.skincare_products_sales_system.enums.RoutineStatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoutineCreateRequest {
    Long bookingOrderId;
    String description;
    String routineName;
    @Enumerated(EnumType.STRING)
    RoutineStatusEnum routineStatus;
    LocalDateTime startDate;
    LocalDateTime endDate;
    List<DailyRoutineRequest> dailyRoutines;
}
