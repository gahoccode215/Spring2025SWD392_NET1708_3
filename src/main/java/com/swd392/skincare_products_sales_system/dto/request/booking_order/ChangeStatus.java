package com.swd392.skincare_products_sales_system.dto.request.booking_order;
import com.swd392.skincare_products_sales_system.enums.BookingStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangeStatus {
    @Enumerated(EnumType.STRING)
    BookingStatus status;
    @NotNull
    String response;
}
