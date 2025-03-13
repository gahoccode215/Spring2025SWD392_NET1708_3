package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.entity.routine.DailyRoutine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyRoutineRepository extends JpaRepository<DailyRoutine, Long> {
}
