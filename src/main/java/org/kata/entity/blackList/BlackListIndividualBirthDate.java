package org.kata.entity.blackList;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Setter
@Getter
public class BlackListIndividualBirthDate {

    public BlackListIndividualBirthDate() {
        birthDate = LocalDate.of(1995, 5, 10);
    }

    private String uuid = UUID.randomUUID().toString();
    private LocalDate birthDate;

}
