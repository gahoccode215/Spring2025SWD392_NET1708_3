package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.enity.booking.ProcessBookingOrder;
import com.swd392.skincare_products_sales_system.repository.ProcessBookingOrderRepository;
import com.swd392.skincare_products_sales_system.service.ProcessBookingOrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProcessBookingOrderImpl implements ProcessBookingOrderService {
    ProcessBookingOrderRepository processBookingOrderRepository;

    @Override
    public List<ProcessBookingOrder> getProcessBookingOrderService() {
        return processBookingOrderRepository.findAll()
                .stream()
                .toList();
    }

    @Override
    public ProcessBookingOrder getProcessBookingOrderServiceById(Long id) {
        Optional<ProcessBookingOrder> processBookingOrder = processBookingOrderRepository.findById(id);
        return processBookingOrder.orElse(null);
    }
}
