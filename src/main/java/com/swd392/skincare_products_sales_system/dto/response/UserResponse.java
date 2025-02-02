package com.swd392.skincare_products_sales_system.dto.response;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse implements Serializable {
    String id;
    String firstName;
    String lastName;
    String username;
//    Date dob;
//    Set<RoleResponse> roles;
}
