package com.swd392.skincare_products_sales_system.dto.response;

import com.swd392.skincare_products_sales_system.entity.booking.BookingOrder;
import com.swd392.skincare_products_sales_system.entity.booking.ImageSkin;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingOrderResponse {
    BookingOrder bookingOrder;
    List<ImageSkin> imageSkin;
}
