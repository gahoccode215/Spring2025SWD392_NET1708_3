package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.model.Quiz;
import com.swd392.skincare_products_sales_system.model.Role;
import com.swd392.skincare_products_sales_system.model.User;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findQuizById(Long quizId);
    Optional<Quiz> findSkincareServiceByIdAndIsDeletedIsFalse(Long quizId);
    Optional<Quiz> findQuizByTitle(String title);
    List<Quiz> findByTitle(String title);
    List<Quiz> findAllByStatus(Status status);
}
