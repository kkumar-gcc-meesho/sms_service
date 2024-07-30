package com.example.notification.utils;


import com.example.notification.exceptions.InvalidPhoneNumberException;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@NoArgsConstructor
public class PhoneNumberUtils {
    protected static final Logger logger = LogManager.getLogger();

    private static final String defaultRegion = "IN";
    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    private Phonenumber.PhoneNumber phoneNumber;

    public PhoneNumberUtils(String phoneNumber) {
        try {
            this.phoneNumber = phoneNumberUtil.parse(phoneNumber, defaultRegion);
        } catch (NumberParseException e) {
            logger.error(e);

            throw new InvalidPhoneNumberException();
        }
    }

    public String getE164Format() {
        if (phoneNumber == null) {
            throw new InvalidPhoneNumberException("phoneNumber is null");
        }

        return phoneNumberUtil.format(this.phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
    }

    public boolean isValid() {
        if (phoneNumber == null) {
            throw new InvalidPhoneNumberException("phoneNumber is null");
        }

        return phoneNumberUtil.isValidNumber(this.phoneNumber);
    }

    public void setPhoneNumber(String phoneNumber) {
        try {
            this.phoneNumber = phoneNumberUtil.parse(phoneNumber, defaultRegion);
        } catch (NumberParseException e) {
            logger.error(e);

            throw new InvalidPhoneNumberException();
        }
    }

    public void resetPhoneNumber() {
        this.phoneNumber = null;
    }

}
