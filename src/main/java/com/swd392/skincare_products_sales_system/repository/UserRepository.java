package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "select u from User u where u.isDeleted=false " +
            "and (lower(u.firstName) like :keyword " +
            "or lower(u.lastName) like :keyword " +
            "or lower(u.username) like :keyword " +
            "or lower(u.phone) like :keyword " +
            "or lower(u.email) like :keyword)")
    Page<User> searchByKeyword(String keyword, Pageable pageable);

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByIdAndIsDeletedFalse(String userId);

    Page<User> findAllByIsDeletedFalse(Pageable pageable);
}
