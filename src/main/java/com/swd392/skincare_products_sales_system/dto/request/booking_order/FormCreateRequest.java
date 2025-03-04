package com.swd392.skincare_products_sales_system.dto.request.booking_order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.swd392.skincare_products_sales_system.enums.SkinType;
import com.swd392.skincare_products_sales_system.model.ImageSkin;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormCreateRequest {
    @NotNull(message = "Skincare Service ID cannot be null")
    Long skincareServiceId;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "SkinType cannot be null")
    SkinType skinType;
    String note;
    String skinCondition;
    String allergy;
    LocalDateTime bookDate;
    String expertId;
    LocalDateTime date;
    @NotNull(message = "LastName cannot be null")
    String lastName;

    @NotNull(message = "FirstName cannot be null")
    String firstName;

    Integer age;

    List<ImageSkinRequest> imageSkins;
}
