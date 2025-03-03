package com.swd392.skincare_products_sales_system.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpertResponse {
    String id;
    String firstName;
    String lastName;
    String email;
    String phone;
    String address;
    String avatar;
    String gender;
    String role;
}
