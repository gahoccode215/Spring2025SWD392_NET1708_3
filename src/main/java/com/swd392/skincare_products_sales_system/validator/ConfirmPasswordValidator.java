package com.swd392.skincare_products_sales_system.validator;

import com.swd392.skincare_products_sales_system.dto.request.authentication.ChangePasswordRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPasswordConstraint, ChangePasswordRequest> {

    @Override
    public void initialize(ConfirmPasswordConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(ChangePasswordRequest request, ConstraintValidatorContext constraintValidatorContext) {
        if (request == null) {
            return false;
        }

        // Kiểm tra mật khẩu mới có trùng với confirm password không
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("INVALID_CONFIRM_PASSWORD")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
