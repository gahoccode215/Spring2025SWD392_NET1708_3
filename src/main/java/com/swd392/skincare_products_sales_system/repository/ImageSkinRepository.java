package com.swd392.skincare_products_sales_system.repository;

import com.swd392.skincare_products_sales_system.entity.booking.ImageSkin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageSkinRepository extends JpaRepository<ImageSkin, Long> {
    List<ImageSkin> findAllByBookingOrderId(Long bookingOrderId);
}
