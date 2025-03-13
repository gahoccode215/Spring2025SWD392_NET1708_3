package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.entity.quiz.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByIdAndIsDeletedFalse(Long questionId);

}
