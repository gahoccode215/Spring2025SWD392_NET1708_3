package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.enity.booking.ProcessBookingOrder;

import java.util.List;

public interface ProcessBookingOrderService {
    List<ProcessBookingOrder> getProcessBookingOrderService();
    ProcessBookingOrder getProcessBookingOrderServiceById(Long id);
}
