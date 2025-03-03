package com.swd392.skincare_products_sales_system.dto.request.routine;

import com.swd392.skincare_products_sales_system.model.DailyRoutine;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DailyRoutineRequest {
    private LocalDate date;
    private List<StepRequest> steps;
}
