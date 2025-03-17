package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.entity.routine.DailyRoutine;
import com.swd392.skincare_products_sales_system.entity.routine.Routine;

public interface DailyService {
    DailyRoutine updateDailyRoutine(Long dailyRoutineId);
}
