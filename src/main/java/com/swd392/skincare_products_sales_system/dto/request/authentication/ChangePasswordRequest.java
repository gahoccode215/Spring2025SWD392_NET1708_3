package com.swd392.skincare_products_sales_system.dto.request.authentication;
import com.swd392.skincare_products_sales_system.validator.ConfirmPasswordConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfirmPasswordConstraint
public class ChangePasswordRequest {

    @Size(min = 6, message = "INVALID_PASSWORD")
    String oldPassword;

    @Size(min = 6, message = "INVALID_PASSWORD")
    String newPassword;

    @Size(min = 6, message = "INVALID_PASSWORD")
    String confirmPassword;
}
