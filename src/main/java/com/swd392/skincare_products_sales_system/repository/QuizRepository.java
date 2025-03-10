package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findQuizById(Long quizId);
    Optional<Quiz> findSkincareServiceByIdAndIsDeletedIsFalse(Long quizId);
    Optional<Quiz> findQuizByTitle(String title);
    List<Quiz> findByTitle(String title);
    List<Quiz> findAllByStatus(Status status);
}
