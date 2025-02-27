package com.swd392.skincare_products_sales_system.dto.request.booking_order;

import com.swd392.skincare_products_sales_system.enums.SkinType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FormCreateRequest {
    @NotNull(message = "Skincare Service ID cannot be null")
    Long skincareServiceId;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "SkinType cannot be null")
    SkinType skinType;
    String note;
    String skinCondition;
    @NotNull(message = "Image cannot be null")
    String image;
    String allergy;
    LocalDateTime bookDate;
    String expertId;
    LocalDateTime date;

}
