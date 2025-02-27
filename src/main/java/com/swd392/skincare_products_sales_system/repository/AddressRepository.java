package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.model.Address;
import com.swd392.skincare_products_sales_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);
    Address findByUserAndIsDefaultTrue(User user);
}
