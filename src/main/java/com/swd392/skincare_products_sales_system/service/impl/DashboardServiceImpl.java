package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.dto.response.DashboardResponse;
import com.swd392.skincare_products_sales_system.dto.response.TopSellingProductResponse;
import com.swd392.skincare_products_sales_system.enums.OrderStatus;
import com.swd392.skincare_products_sales_system.repository.OrderItemRepository;
import com.swd392.skincare_products_sales_system.repository.OrderRepository;
import com.swd392.skincare_products_sales_system.repository.UserRepository;
import com.swd392.skincare_products_sales_system.service.DashboardService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashboardServiceImpl implements DashboardService {

    UserRepository userRepository;
    OrderRepository orderRepository;
    OrderItemRepository orderItemRepository;

    @Override
    public DashboardResponse getDashboardData() {
        DashboardResponse dashboard = new DashboardResponse();
        Long totalCustomer = userRepository.countByRoleCustomer();
        dashboard.setTotalCustomer(totalCustomer != null ? totalCustomer : 0);
        Double totalRevenue = orderRepository.sumTotalAmount();
        dashboard.setTotalRevenue(totalRevenue != null ? totalRevenue : 0.0);
        Long totalOrder = orderRepository.countOrdersByStatus(OrderStatus.DONE);
        dashboard.setTotalOrder(totalOrder != null ? totalOrder : 0);
        Long totalQuantitySold = orderItemRepository.getTotalQuantitySold();
        dashboard.setTotalQuantitySold(totalQuantitySold != null ? totalQuantitySold : 0);
        List<TopSellingProductResponse> topSellingProductResponses = orderItemRepository.findTopSellingProducts(5);
        dashboard.setTopSellingProduct(topSellingProductResponses);
        return dashboard;
    }
}
