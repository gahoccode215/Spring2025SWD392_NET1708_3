package com.swd392.skincare_products_sales_system.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {RoleValidator.class})
public @interface RoleConstraint {
    String message() default "Quyền không chính xác";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
