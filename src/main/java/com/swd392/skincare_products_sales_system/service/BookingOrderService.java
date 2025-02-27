package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.booking_order.AsignExpertRequest;
import com.swd392.skincare_products_sales_system.dto.request.booking_order.ChangeStatus;
import com.swd392.skincare_products_sales_system.dto.request.booking_order.FormCreateRequest;
import com.swd392.skincare_products_sales_system.dto.request.booking_order.FormUpdateRequest;
import com.swd392.skincare_products_sales_system.dto.response.FormResponse;
import com.swd392.skincare_products_sales_system.model.BookingOrder;
import com.swd392.skincare_products_sales_system.model.User;

import java.util.List;

public interface BookingOrderService {
    //Booking
    FormResponse bookingAdvise(FormCreateRequest request);
    FormResponse updateBookingAdvise(FormUpdateRequest request, Long bookingOrderId);
    BookingOrder changeStatus(ChangeStatus status);
    List<BookingOrder> getBookingOrder(); // View
    List<User> filterListExpert();
    BookingOrder asignBookingOrder(AsignExpertRequest request);

}
