package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.routine.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
    Optional<Routine> findByIdAndIsDeletedFalse(Long Id);
}
