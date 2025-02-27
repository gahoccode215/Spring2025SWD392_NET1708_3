package com.swd392.skincare_products_sales_system.dto.request;
import com.swd392.skincare_products_sales_system.enums.BookingStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangeStatus {
    Long bookingOrderId;
    @Enumerated(EnumType.STRING)
    BookingStatus status;
    String image;
    String note;
}
