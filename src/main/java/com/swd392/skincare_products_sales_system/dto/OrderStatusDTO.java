package com.swd392.skincare_products_sales_system.dto;

public class OrderStatusDTO {
    private String statusLabel;  // Trạng thái đơn hàng dưới dạng String
    private Long orderCount;     // Số lượng đơn hàng

    // Constructor không có tham số (mặc định)
    public OrderStatusDTO() {
    }

    public OrderStatusDTO(String statusLabel, Long orderCount) {
        this.statusLabel = statusLabel;
        this.orderCount = orderCount;
    }

    // Getter và Setter
    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public Long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }
}
