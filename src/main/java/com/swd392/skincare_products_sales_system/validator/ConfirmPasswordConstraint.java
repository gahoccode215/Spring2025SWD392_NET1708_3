package com.swd392.skincare_products_sales_system.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE, FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ConfirmPasswordValidator.class})
public @interface ConfirmPasswordConstraint {
    String message() default "Mật khẩu xác nhận không chính xác";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
