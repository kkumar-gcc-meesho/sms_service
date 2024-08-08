package com.example.notification.annotations;

import com.example.notification.utils.PhoneNumberUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    private String message;

    @Override
    public void initialize(PhoneNumber requiredIfChecked) {
        this.message = requiredIfChecked.message();
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid = false;

        if (value != null && !value.isEmpty()) {
            PhoneNumberUtils phoneNumberUtil = new PhoneNumberUtils(value);

            isValid = phoneNumberUtil.isValid();
        }

        if (!isValid){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        }

        return isValid;
    }
}
