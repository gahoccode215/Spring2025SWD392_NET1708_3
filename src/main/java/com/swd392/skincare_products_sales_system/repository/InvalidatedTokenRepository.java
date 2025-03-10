package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.authentication.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
    boolean existsByToken(String token);
}
