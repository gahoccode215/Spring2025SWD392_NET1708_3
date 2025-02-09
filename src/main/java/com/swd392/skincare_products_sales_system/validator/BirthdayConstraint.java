package com.swd392.skincare_products_sales_system.validator;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = {BirthdayValidator.class})
public @interface BirthdayConstraint {
    String message() default "Invalid birthday";

    int min();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
