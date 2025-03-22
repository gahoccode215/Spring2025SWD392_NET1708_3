package com.swd392.skincare_products_sales_system.dto.request.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.swd392.skincare_products_sales_system.enums.Gender;
import com.swd392.skincare_products_sales_system.validator.BirthdayConstraint;
import com.swd392.skincare_products_sales_system.validator.GenderConstraint;
import com.swd392.skincare_products_sales_system.validator.RoleConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCreationRequest {
    String avatar;
    String firstName;
    String lastName;
    LocalDate birthday;
    Gender gender;
    String username;
    String password;
    String phone;
    String roleName;
}
