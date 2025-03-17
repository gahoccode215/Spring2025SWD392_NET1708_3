package com.swd392.skincare_products_sales_system.enums;


import lombok.Getter;
import lombok.Setter;

@Getter
public enum BookingStatus {
    PENDING,
    PAYMENT,
    ASSIGNED_EXPERT,
    EXPERT_UPDATE_ORDER,
    CONTACT_CUSTOMER,
    EXPERT_MAKE_ROUTINE,
    CUSTOMER_CONFIRM,
    IN_PROGRESS_ROUTINE,
    FINISHED_ROUTINE,
    CANCELED,
    FINISHED,
}


