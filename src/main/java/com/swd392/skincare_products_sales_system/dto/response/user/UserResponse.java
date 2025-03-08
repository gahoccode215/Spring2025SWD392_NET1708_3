package com.swd392.skincare_products_sales_system.dto.response.user;

import com.swd392.skincare_products_sales_system.enums.Gender;

import com.swd392.skincare_products_sales_system.enums.Status;
import com.swd392.skincare_products_sales_system.model.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String firstName;
    Gender gender;
    String lastName;
    String username;
    String email;
    Status status;
    String roleName;
    String avatar;
    Integer point;
    LocalDate birthday;
}
