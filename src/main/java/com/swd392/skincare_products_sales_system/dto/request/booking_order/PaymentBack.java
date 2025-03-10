package com.swd392.skincare_products_sales_system.dto.request.booking_order;

import com.swd392.skincare_products_sales_system.enums.PaymentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class PaymentBack {
    Long bookingOrderId;
    Boolean isPaid;
}
