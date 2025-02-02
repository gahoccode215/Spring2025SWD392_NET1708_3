package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByIdAndIsDeletedFalse(String userId);
}
