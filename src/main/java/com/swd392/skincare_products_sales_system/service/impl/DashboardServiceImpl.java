package com.swd392.skincare_products_sales_system.service.impl;

import com.swd392.skincare_products_sales_system.dto.OrderStatusDTO;
import com.swd392.skincare_products_sales_system.dto.RevenueByTime;
import com.swd392.skincare_products_sales_system.dto.TopSellingProductDTO;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashboardServiceImpl implements DashboardService {

    UserRepository userRepository;
    OrderRepository orderRepository;
    OrderItemRepository orderItemRepository;

    @Override
    public DashboardResponse getDashboardData(LocalDate startDate, LocalDate endDate) {
        log.info("Getting dashboard data");
        DashboardResponse dashboard = new DashboardResponse();

        // Lấy tổng doanh thu từ các đơn hàng
        Double totalRevenue = orderRepository.sumTotalAmount();
        dashboard.setTotalRevenue(totalRevenue != null ? totalRevenue : 0);

        Long totalOrdersDone = orderRepository.countByStatusDone();
        dashboard.setTotalOrdersDone(totalOrdersDone != null ? totalOrdersDone.intValue() : 0);

        Long totalCustomers = userRepository.countByRoleCustomer();
        dashboard.setTotalCustomers(totalCustomers != null ? totalCustomers.intValue() : 0);

        Long totalProductsSold = orderItemRepository.getTotalQuantitySold();
        dashboard.setTotalProductsSold(totalProductsSold != null ? totalProductsSold.intValue() : 0);

//        List<Double> monthlyRevenue = getMonthlyRevenueData();
//        dashboard.setMonthlyRevenue(monthlyRevenue);

        List<RevenueByTime> revenueByTimes = getRevenueByDateRange(startDate, endDate);
        dashboard.setRevenueByTimes(revenueByTimes);

        dashboard.setRevenueByTimes(getRevenueByDateRange(startDate, endDate));


        List<OrderStatusDTO> orderStatuses = getOrderStatuses();
        dashboard.setOrderStatuses(orderStatuses);

        List<TopSellingProductDTO> topSellingProducts = getTopSellingProducts(5);
        dashboard.setTopSellingProducts(topSellingProducts);
        return dashboard;
    }


    private List<OrderStatusDTO> getOrderStatuses() {
        // Truy vấn từ cơ sở dữ liệu để lấy số lượng đơn hàng theo trạng thái
        List<OrderStatus> statuses = List.of(
                OrderStatus.DELIVERING,
                OrderStatus.DONE,
                OrderStatus.PROCESSING,
                OrderStatus.CANCELLED,
                OrderStatus.DELIVERING_FAIL,
                OrderStatus.PENDING
        );

        return statuses.stream()
                .map(status -> new OrderStatusDTO(status.getLabel(), orderRepository.countOrdersByStatus(status)))
                .collect(Collectors.toList());
    }

    private List<TopSellingProductDTO> getTopSellingProducts(int topCount) {
        // Get the top-selling products
        List<Object[]> topSellingProducts = orderItemRepository.getTopSellingProducts();
        List<TopSellingProductDTO> result = new ArrayList<>();

        for (int i = 0; i < Math.min(topCount, topSellingProducts.size()); i++) {
            Object[] row = topSellingProducts.get(i);
            String productName = (String) row[0];
            Long quantitySold = (Long) row[1];

            result.add(new TopSellingProductDTO(productName, quantitySold));
        }

        return result;
    }

    public List<RevenueByTime> getRevenueByDateRange(LocalDate startDate, LocalDate endDate) {
        // Nếu startDate hoặc endDate là null, lấy giá trị mặc định từ cơ sở dữ liệu
        if (startDate == null) {
            startDate = orderRepository.getMinOrderDate(OrderStatus.DONE).atStartOfDay().toLocalDate();
        }
        if (endDate == null) {
            endDate = orderRepository.getMaxOrderDate(OrderStatus.DONE).atStartOfDay().toLocalDate();
        }

        // Chuyển đổi LocalDate thành LocalDateTime (đầu ngày cho startDate, cuối ngày cho endDate)
        LocalDateTime startDateTime = startDate.atStartOfDay();  // 00:00:00 của ngày startDate
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59, 999999999);  // 23:59:59.999999999 của ngày endDate

        // Lấy dữ liệu doanh thu từ repository
        List<Object[]> result = orderRepository.getRevenueByDateRange(OrderStatus.DONE, startDateTime, endDateTime);

        // Ánh xạ kết quả vào DTO RevenueByTime
        return result.stream()
                .map(row -> new RevenueByTime(
                        ((LocalDateTime) row[0]).toLocalDate(),  // Chuyển LocalDateTime về LocalDate
                        ((Double) row[1])            // Chuyển Double thành Long
                ))
                .collect(Collectors.toList());
    }
}
