package com.example.notification.annotations;


import com.example.notification.constants.Message;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { PhoneNumberValidator.class })
@Target({ElementType.TYPE_USE, ElementType.FIELD, ElementType.TYPE})
public @interface PhoneNumber {

    String message() default Message.ERROR_PHONE_NUMBER_INVALID;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value() default "";
}
