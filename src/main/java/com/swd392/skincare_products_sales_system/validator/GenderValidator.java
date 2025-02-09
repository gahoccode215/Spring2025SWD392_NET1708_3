package com.swd392.skincare_products_sales_system.validator;

import com.swd392.skincare_products_sales_system.enums.Gender;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GenderValidator implements ConstraintValidator<GenderConstraint, Gender> {

    @Override
    public void initialize(GenderConstraint constraintAnnotation) {
        // Thực hiện khởi tạo nếu cần
    }

    @Override
    public boolean isValid(Gender value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        // Kiểm tra nếu giá trị có nằm trong các giá trị hợp lệ của Gender hay không
        try {
            Gender.valueOf(value.name());  // Kiểm tra nếu giá trị có hợp lệ trong enum
            return true;
        } catch (IllegalArgumentException e) {
            return false;  // Nếu không hợp lệ, trả về false
        }
    }
}
