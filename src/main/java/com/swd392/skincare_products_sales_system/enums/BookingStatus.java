package com.swd392.skincare_products_sales_system.enums;


import lombok.Getter;
import lombok.Setter;

@Getter
public enum BookingStatus {
    PENDING,
    PAYMENT,
    ASSIGNED_EXPERT,
    EXPERT_RECEIVED,
    CONTACT_CUSTOMER,
    CUSTOMER_CONFIRM,
    EXPERT_MAKE_ROUTINE,
    IN_PROGRESS_ROUTINE,
    CANCELED,
    FINISHED,
}


