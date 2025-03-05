package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.booking_order.*;
import com.swd392.skincare_products_sales_system.dto.response.ExpertResponse;
import com.swd392.skincare_products_sales_system.dto.response.FormResponse;
import com.swd392.skincare_products_sales_system.dto.response.PaymentOrderResponse;
import com.swd392.skincare_products_sales_system.enums.PaymentMethod;
import com.swd392.skincare_products_sales_system.model.BookingOrder;
import com.swd392.skincare_products_sales_system.model.User;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface BookingOrderService {
    //Booking
    FormResponse bookingAdvise(FormCreateRequest request);
    FormResponse updateBookingAdvise(FormUpdateRequest request, Long bookingOrderId);
    BookingOrder changeStatus(ChangeStatus status);
    List<BookingOrder> getBookingOrder(); // View
    List<BookingOrder> getBookingOrderByExpertId();
    List<ExpertResponse> filterListExpert();
    BookingOrder asignBookingOrder(AsignExpertRequest request, Long bookingOrderId);
    PaymentOrderResponse paymentBookingOrder(PaymentBookingOrderRequest paymentBookingOrderRequest, Long bookingOrderId, String isAddress) throws UnsupportedEncodingException;
    List<BookingOrder> listAllBookingOrder();
}
