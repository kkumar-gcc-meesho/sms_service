package com.example.notification.annotations;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { PhoneNumberValidator.class })
public @interface PhoneNumber {

    String message() default "Field value should be from list of ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String propName();

    String[] values();
}
