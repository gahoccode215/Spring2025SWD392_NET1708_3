package com.swd392.skincare_products_sales_system.dto.request;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.swd392.skincare_products_sales_system.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SkincareCreateRequest {
    String serviceName;
    String description;
    Float price;
    Status status;

}
