package com.swd392.skincare_products_sales_system.service;


import com.swd392.skincare_products_sales_system.enums.RoutineStatusEnum;
import com.swd392.skincare_products_sales_system.model.routine.Step;

public interface StepService {
    Step updateStep(Long stepId);
}
