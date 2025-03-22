package com.swd392.skincare_products_sales_system.enums;

public enum OrderStatus {
    PENDING("Chờ xử lý"),
    CANCELLED("Đã hủy"),
    PROCESSING("Đang xử lý"),
    DELIVERING("Đang giao"),
    DELIVERING_FAIL("Giao thất bại"),
    DONE("Đã giao");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
