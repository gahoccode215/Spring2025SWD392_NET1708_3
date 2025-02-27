package com.swd392.skincare_products_sales_system.enums;


import lombok.Getter;
import lombok.Setter;

@Getter
public enum BookingStatus {
    PENDING,
    PAYMENT,
    //Thanh toán thành công chuyển sang system nếu expert null
    PAYMENT_SUCCESS,
    // Từ Payemnt nếu được chỉ định, rồi mới tới system
    ASSIGNED_EXPERT,
    CONTACT_CUSTOMER,
    // 1: là kết thúc tư vấn || Nếu mua liệu trình
    CUSTOMER_CONFIRM,
    // Mua lộ trình
    PENDING_CONFIRM,
    EXPERT_MAKE_ROUTINE,
    IN_PROGRESS_ROUTINE,
    PAYMENT_ROUTINE,
    // Status
    CANCELED,
    FINISHED,

}


