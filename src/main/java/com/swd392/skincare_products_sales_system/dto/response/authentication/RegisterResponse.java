package com.swd392.skincare_products_sales_system.dto.response.authentication;

import com.swd392.skincare_products_sales_system.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterResponse {
    String id;
    String username;
    Gender gender;
    LocalDate birthday;
    String email;
}
