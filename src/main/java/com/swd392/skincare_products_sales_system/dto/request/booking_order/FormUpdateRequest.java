package com.swd392.skincare_products_sales_system.dto.request.booking_order;

import com.swd392.skincare_products_sales_system.enums.SkinType;
import com.swd392.skincare_products_sales_system.model.ImageSkin;
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
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FormUpdateRequest {
    @NotNull(message = "Booking Order ID cannot be null")
    Long id;
    @NotNull(message = "Skincare Service ID cannot be null")
    Long skincareServiceId;
    @Enumerated(EnumType.STRING)
    SkinType skinType;
    String note;
    String skinCondition;
    String allergy;
    LocalDateTime bookDate;
    LocalDateTime date;
    String expertId;
    String firstName;
    String lastName;
    Integer age;
    List<ImageSkinRequest> imageSkin;
}
