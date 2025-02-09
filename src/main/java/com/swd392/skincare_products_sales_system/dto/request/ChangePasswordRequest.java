package com.swd392.skincare_products_sales_system.dto.request;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
//@ConfirmPasswordConstraint
public class ChangePasswordRequest {

    String oldPassword;

    @Size(min = 6, message = "INVALID_PASSWORD")
    String newPassword;

    @Size(min = 6, message = "INVALID_PASSWORD")
    String confirmPassword;
}
