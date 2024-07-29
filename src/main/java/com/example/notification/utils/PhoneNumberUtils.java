package com.example.notification.utils;


import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PhoneNumberUtils {
    private static final String defaultRegion = "IN";
    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    private Phonenumber.PhoneNumber phoneNumber;

    public PhoneNumberUtils(String phoneNumber) {
        try {
            this.phoneNumber = phoneNumberUtil.parse(phoneNumber, defaultRegion);
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e);
        }
    }

    public String getE164Format() {
        if (phoneNumber == null) {
            throw new IllegalStateException("phoneNumber is null");
        }

        return phoneNumberUtil.format(this.phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
    }

    public boolean isValid() {
        if (phoneNumber == null) {
            throw new IllegalStateException("phoneNumber is null");
        }

        return phoneNumberUtil.isValidNumber(this.phoneNumber);
    }

    public void setPhoneNumber(String phoneNumber) {
        try {
            this.phoneNumber = phoneNumberUtil.parse(phoneNumber, defaultRegion);
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e);
        }
    }

    public void resetPhoneNumber() {
        this.phoneNumber = null;
    }

}
