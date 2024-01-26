package org.kata.util;

import io.lettuce.core.dynamic.annotation.CommandNaming;
import org.kata.exception.InvalidPhoneNumberException;
import org.springframework.stereotype.Component;

@Component
public class PhoneNumberValidator {

    public static void validatePhoneNumbers(String phoneNumber) {
        if (!isValidPhoneNumber(phoneNumber)) {
            throw new InvalidPhoneNumberException("Invalid phone number");
        }
    }

    private static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.replaceAll("\\D", "").length() != 11) {
            return false;
        }

        return phoneNumber.matches("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$");
    }
}