package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.entity.user.Address;
import com.swd392.skincare_products_sales_system.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);
}
