package com.swd392.skincare_products_sales_system.dto.request.booking_order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.swd392.skincare_products_sales_system.enums.PaymentStatus;
import com.swd392.skincare_products_sales_system.enums.SkinType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
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
    PaymentStatus paymentStatus;

}
