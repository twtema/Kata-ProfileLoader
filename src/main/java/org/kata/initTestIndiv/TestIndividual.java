package org.kata.initTestIndiv;

import lombok.Getter;
import lombok.Setter;
import org.kata.entity.*;
import org.kata.entity.enums.GenderType;

import java.util.*;

@Getter
@Setter
public class TestIndividual extends Individual {
    private final String uuid = UUID.randomUUID().toString();
    private final String icp = "100-200-300";
    private final String name = "Петр";
    private final String surname = "Давыдов";
    private final String patronymic = "Иванович";
    private final String fullName = "Давыдов Петр Иванович";
    private final GenderType gender = GenderType.MALE;
    private final String placeOfBirth = "Казань";
    private final String countryOfBirth = "Россия";
    private final Date birthDate = new Date(92, Calendar.NOVEMBER, 15, 0, 0);
    private List<Document> documents;
    private List<ContactMedium> contacts;
    private List<Address> address;
    private List<Avatar> avatar;
    private List<Account> account;

}
