package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.entity.authentication.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
    List<Role> findAllByNameIn(List<String> names);
}
