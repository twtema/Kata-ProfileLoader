package org.kata.entity.blackList;

import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Setter
@Getter
public class BlackListIndividualBirthDate {

    public BlackListIndividualBirthDate() {
        birthDate = new Date(95, Calendar.MAY, 10);
    }

    private String uuid = UUID.randomUUID().toString();
    private Date birthDate;

}
