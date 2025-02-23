package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.enums.ErrorCode;
import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.exception.AppException;
import com.swd392.skincare_products_sales_system.model.Category;
import com.swd392.skincare_products_sales_system.model.Role;
import com.swd392.skincare_products_sales_system.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {


    boolean existsByUsername(String username);



    default User findByUsernameOrThrow(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
    }

    Optional<User> findByUsername(String username);

    Optional<User> findByIdAndIsDeletedFalse(String userId);

    @Query("SELECT x FROM User x WHERE x.isDeleted = false " +
            "AND (:keyword IS NULL OR " +
            "x.firstName LIKE %:keyword% OR " +
            "x.lastName LIKE %:keyword% OR " +
            "x.username LIKE %:keyword% OR " +
            "x.email LIKE %:keyword%) " +
            "AND (:status IS NULL OR x.status = :status) " +
            "AND (:role IS NULL OR x.role = :role)")
    Page<User> findAllByFilters(
            @Param("keyword") String keyword,
            @Param("status") Status status,
            @Param("role") Role role,
            Pageable pageable);

    @Modifying
    @Query("UPDATE User x SET x.status = :status WHERE x.id = :id AND x.isDeleted = false")
    void updateUserStatus(@Param("id") String id, @Param("status") Status status);
}
