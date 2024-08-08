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
        this.setPhoneNumber(phoneNumber);
    }

    public String getE164Format() {
        if (phoneNumber == null) {
            throw new InvalidPhoneNumberException();
        }

        return phoneNumberUtil.format(this.phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
    }

    public boolean isValid() {
        if (phoneNumber == null) {
            throw new InvalidPhoneNumberException();
        }

        // SMSs can be sent to MOBILE or FIXED_LINE_OR_MOBILE numbers. So considering only these number as valid.
        PhoneNumberUtil.PhoneNumberType numberType = phoneNumberUtil.getNumberType(this.phoneNumber);
        return numberType == PhoneNumberUtil.PhoneNumberType.MOBILE || numberType == PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE;
    }

    public void setPhoneNumber(String phoneNumber) {
        try {
            this.phoneNumber = phoneNumberUtil.parse(phoneNumber, defaultRegion);
        } catch (NumberParseException e) {
            logger.error(e);

            throw new InvalidPhoneNumberException();
        }
    }

}
