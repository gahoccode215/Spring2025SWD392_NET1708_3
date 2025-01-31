package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM tbl_user u WHERE u.username LIKE %:keyword% OR u.email LIKE %:keyword%")
    Page<UserEntity> searchByKeyword(String keyword, Pageable pageable);

    UserEntity findByUsername(String username);

    UserEntity findByEmail(String email);
}
