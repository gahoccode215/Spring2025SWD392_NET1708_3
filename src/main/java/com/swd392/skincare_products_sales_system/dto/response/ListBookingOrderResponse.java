package com.swd392.skincare_products_sales_system.dto.response;

import com.swd392.skincare_products_sales_system.entity.booking.BookingOrder;
import com.swd392.skincare_products_sales_system.entity.booking.ImageSkin;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListBookingOrderResponse {
    List<BookingOrderResponse> listBooking;
}
