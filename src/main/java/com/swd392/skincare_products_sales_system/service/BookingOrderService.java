package com.swd392.skincare_products_sales_system.service;

import com.swd392.skincare_products_sales_system.dto.request.ChangeStatus;
import com.swd392.skincare_products_sales_system.dto.request.FormCreateRequest;
import com.swd392.skincare_products_sales_system.dto.response.FormResponse;
import com.swd392.skincare_products_sales_system.model.User;

import java.util.List;

public interface BookingOrderService {
    //Booking
    FormResponse bookingAdvise(FormCreateRequest request);
    com.swd392.skincare_products_sales_system.model.BookingOrder changeStatus(ChangeStatus status);
    List<com.swd392.skincare_products_sales_system.model.BookingOrder> getBookingOrder(); // View
    List<User> filterListExpert();

}
