package com.swd392.skincare_products_sales_system.dto.response;

import com.swd392.skincare_products_sales_system.enums.BookingStatus;
import com.swd392.skincare_products_sales_system.enums.SkinType;
import com.swd392.skincare_products_sales_system.model.ImageSkin;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FormResponse {
    Long skincareService;
    SkinType skinType;
    String note;
    String skinCondition;
        String allergy;
    LocalDateTime bookDate;
    String userId;
    String expertName;
    Float price;
    BookingStatus status;
    LocalDateTime date;
    String firstName;
    String lastName;
    Integer age;
    List<ImageSkin> imageSkins;
}
