package com.swd392.skincare_products_sales_system.dto.request.daily_routine;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DailyRoutineRequest {

    LocalDateTime dateTime;

}
