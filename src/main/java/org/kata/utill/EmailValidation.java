package org.kata.utill;

import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.kata.controller.dto.ContactMediumDto;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.enums.ContactMediumType;
import org.kata.exception.InvalidEmailException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class EmailValidation {

    EmailValidator emailValidator = EmailValidator.getInstance();

    ConcurrentHashMap<Integer, String> blacklistEmails = new ConcurrentHashMap<>() {{
        put(0, "genapoh562@rentaen.com");
        put(1, "jijahi2076@ikuromi.com");
        put(2, "cosexef830@ikuromi.com");
        put(3, "gigeb46497@ikuromi.com");
        put(4, "vebad91429@grassdev.com");
        put(5, "sojab62273@ikuromi.com");
    }};


    public boolean emailSearch(IndividualDto dto) {
        List<ContactMediumDto> contactMedium = dto.getContacts();

        for (ContactMediumDto contact : contactMedium) {
            if (contact.getType().equals(ContactMediumType.EMAIL)) {
                return true;
            }
        }

        return false;
    }

    public boolean emailSearch(ContactMediumDto dto) {
        return dto.getType().equals(ContactMediumType.EMAIL);
    }

    public List<String> getEmailValue(IndividualDto dto) {
        return dto.getContacts().stream().filter(contact ->
                contact.getType().equals(ContactMediumType.EMAIL)).map(ContactMediumDto::getValue).toList();
    }

    public String getEmailValue(ContactMediumDto dto) {
        return dto.getValue();
    }

    public void validateEmail(List<String> emails) {
        for (String email : emails) {
            if (!emailValidator.isValid(email) || blacklistEmails.contains(email)) {
                throw new InvalidEmailException("Invalid email");
            }
        }
    }

    public void validateEmail(String email) {
        if (!emailValidator.isValid(email) || blacklistEmails.contains(email)) {
            throw new InvalidEmailException("Invalid email");
        }
    }


}
