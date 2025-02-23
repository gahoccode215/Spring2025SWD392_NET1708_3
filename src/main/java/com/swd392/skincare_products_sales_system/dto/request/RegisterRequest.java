package com.swd392.skincare_products_sales_system.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.swd392.skincare_products_sales_system.enums.Gender;
import com.swd392.skincare_products_sales_system.validator.BirthdayConstraint;
import com.swd392.skincare_products_sales_system.validator.GenderConstraint;
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
public class RegisterRequest  {

    @Size(min = 6, message = "INVALID_USERNAME")
    String username;

    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;

    @BirthdayConstraint(min = 6, message = "INVALID_BIRTHDAY")
    LocalDate birthday;

    @GenderConstraint(message = "INVALID_GENDER")
    Gender gender;
}
