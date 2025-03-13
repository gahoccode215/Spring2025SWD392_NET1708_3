package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.entity.routine.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface StepRepository extends JpaRepository<Step, Long> {
    @Query("SELECT s FROM Step s WHERE s.dailyRoutine.date = :date")
    List<Step> findByDailyRoutineDate(@Param("date") LocalDate date);
}
