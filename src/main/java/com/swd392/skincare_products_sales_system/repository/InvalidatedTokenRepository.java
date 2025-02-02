package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
    Optional<InvalidatedToken> findByToken(String token);
}
