package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.dto.response.BlogResponse;
import com.swd392.skincare_products_sales_system.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    Optional<Blog> findById(Long blogId);
    Optional<Blog> findBlogByBlogNameAndIsDeletedFalse(String blogName);
}
