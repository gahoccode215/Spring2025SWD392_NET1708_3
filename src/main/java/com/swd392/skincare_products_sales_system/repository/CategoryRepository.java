package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

}
