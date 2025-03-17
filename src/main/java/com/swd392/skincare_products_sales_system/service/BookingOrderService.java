package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.booking_order.*;
import com.swd392.skincare_products_sales_system.dto.response.*;
import com.swd392.skincare_products_sales_system.entity.booking.BookingOrder;
import com.swd392.skincare_products_sales_system.entity.product.Product;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface BookingOrderService {
    //Booking
    FormResponse bookingAdvise(FormCreateRequest request);
    FormResponse updateBookingAdvise(FormUpdateRequest request, Long bookingOrderId);
    BookingOrder changeStatus(ChangeStatus status, Long bookingOrderId);
    List<BookingOrder> getBookingOrder(); // View
    List<BookingOrderResponse> getBookingOrderByExpertId();
    List<ExpertResponse> filterListExpert();
    BookingOrder asignBookingOrder(AsignExpertRequest request, Long bookingOrderId);
    PaymentOrderResponse paymentBookingOrder(Long bookOrderId,String isAddress) throws UnsupportedEncodingException;
    List<BookingOrder> listAllBookingOrder();
    BookingOrder cancelBookingOrder(Long bookingOrderId, String note);
    String updateBookingOrderStatus(PaymentBack paymentBack);
    BookingOrderResponse getBookingOrderById(Long bookingOrderId);
    List<Product> getProducts();

}
