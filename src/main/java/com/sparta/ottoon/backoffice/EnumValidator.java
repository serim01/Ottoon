package com.sparta.ottoon.backoffice;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<Enum, java.lang.Enum> {
    private Enum annotation;

    @Override
    public void initialize(Enum constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(java.lang.Enum value, ConstraintValidatorContext context) {
        Object[] enumValues = this.annotation.enumclass().getEnumConstants();
        if (enumValues != null) {
            for (Object enumValue : enumValues) {
                if (value.equals(enumValue.toString())) {
                    return true;
                }
            }
        }
        return false;
    }
}