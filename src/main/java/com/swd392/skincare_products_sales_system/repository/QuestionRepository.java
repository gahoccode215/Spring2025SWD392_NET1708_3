package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.Question;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByIdAndIsDeletedFalse(Long questionId);

}
