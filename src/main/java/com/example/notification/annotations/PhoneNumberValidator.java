package com.example.notification.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    private String propName;
    private String message;
    private List<String> allowable;

    @Override
    public void initialize(PhoneNumber requiredIfChecked) {
        this.propName = requiredIfChecked.propName();
        this.message = requiredIfChecked.message();
        this.allowable = Arrays.asList(requiredIfChecked.values());
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean valid = this.allowable.contains(value);

        if (!Boolean.TRUE.equals(valid)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message.concat(this.allowable.toString()))
                    .addPropertyNode(this.propName).addConstraintViolation();
        }
        return valid;
    }
}
