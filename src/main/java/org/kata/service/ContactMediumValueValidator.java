package org.kata.service;

import org.kata.controller.dto.ContactMediumDto;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ContactMediumValueValidator {
    private final Pattern validEmailAddressRegex =
            Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", Pattern.CASE_INSENSITIVE);
    private final Pattern validPhoneNumberRegex =
            Pattern.compile("^\\+?\\d[ \\-(]?\\d{3}[ \\-)]?\\d{3}([ \\-]?\\d{2}){2}$");

    public boolean validateDtoContactValue(ContactMediumDto dto) {
        switch (dto.getType()) {
            case EMAIL -> {
                return validateContactEmail(dto.getValue());
            }
            case PHONE -> {
                return validateContactPhoneNumber(dto.getValue());
            }
            default -> {
                return false;
            }
        }
    }

    private boolean validateContactEmail(String email) {
        Matcher matcher = validEmailAddressRegex.matcher(email);
        return matcher.matches();
    }

    private boolean validateContactPhoneNumber(String phoneNumber) {
        Matcher matcher = validPhoneNumberRegex.matcher(phoneNumber);
        return matcher.matches();
    }
}
