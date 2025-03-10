package com.swd392.skincare_products_sales_system.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Đánh dấu annotation này sẽ được sử dụng để kiểm tra trường Gender.
@Constraint(validatedBy = GenderValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface GenderConstraint {
    String message() default "Giới tính không chính xác";  // Thông báo lỗi mặc định
    Class<?>[] groups() default {};  // Dùng cho các nhóm kiểm tra
    Class<? extends Payload>[] payload() default {};  // Dùng cho payload
}
