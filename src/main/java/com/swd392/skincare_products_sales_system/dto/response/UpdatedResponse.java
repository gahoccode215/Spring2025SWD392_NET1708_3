package com.swd392.skincare_products_sales_system.dto.response;

import com.swd392.skincare_products_sales_system.model.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatedResponse {
    User updatedBy;
    LocalDateTime updatedAt;
}
