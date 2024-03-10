package org.kata.entity.blackList;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;
@Component
@Setter
@Getter
public class BlackListIndividualBirthDate {

    public BlackListIndividualBirthDate() {
        birthDate = LocalDate.of(1995, 5, 10);
    }

    private String uuid = UUID.randomUUID().toString();
    private LocalDate birthDate;
}
