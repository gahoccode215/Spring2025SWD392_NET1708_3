package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.entity.SkincareService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkincareServiceRepository extends JpaRepository<SkincareService,Long> {
    Optional<SkincareService> findSkincareServiceByServiceName(String serviceName);
    Optional<SkincareService> findSkincareServiceByIdAndIsDeletedIsFalse(Long id);
    Optional<SkincareService> findSkincareServiceById(Long  id);

}
