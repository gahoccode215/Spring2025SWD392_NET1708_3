package com.swd392.skincare_products_sales_system.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class BirthdayValidator implements ConstraintValidator<BirthdayConstraint, LocalDate> {

    private int min;

    @Override
    public void initialize(BirthdayConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(value)) return false;

        long years = ChronoUnit.YEARS.between(value, LocalDate.now());

        return years >= min;
    }
}
