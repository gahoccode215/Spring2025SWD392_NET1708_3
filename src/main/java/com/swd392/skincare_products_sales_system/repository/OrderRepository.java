package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
