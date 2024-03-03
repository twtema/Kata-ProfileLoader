package org.kata.initTestIndiv;

import lombok.Getter;
import lombok.Setter;
import org.kata.entity.ContactMedium;
import org.kata.entity.Individual;
import org.kata.entity.enums.ContactMediumType;

import java.util.UUID;

@Getter
@Setter
public class TestContacts extends ContactMedium {
    private final String uuid = UUID.randomUUID().toString();
    private final ContactMediumType type = ContactMediumType.PHONE;
    private final String value = "+7(900)-140-25-13";
    private Individual individual;
    private final boolean isActual = true;
}
