package com.swd392.skincare_products_sales_system.dto.request.booking_order;

import lombok.Data;

@Data
public class PaymentBookingOrderRequest {
    String amount;
    long bookingOrderId;
}
