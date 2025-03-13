package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByIdAndIsDeletedFalse(Long answerId);
}
