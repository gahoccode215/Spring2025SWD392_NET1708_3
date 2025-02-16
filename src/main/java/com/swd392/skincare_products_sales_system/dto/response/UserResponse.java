package com.swd392.skincare_products_sales_system.dto.response;

import java.io.Serializable;
import java.util.Set;

import com.swd392.skincare_products_sales_system.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    Set<RoleResponse> roles;
}
