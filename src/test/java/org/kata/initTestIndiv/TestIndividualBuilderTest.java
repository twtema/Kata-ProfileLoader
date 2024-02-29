package org.kata.initTestIndiv;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kata.entity.Individual;

//Тест на создание индивидуала с заполненными полями

class TestIndividualBuilderTest {

    @Test
    void individualInitializer() {

        Individual individual = new TestIndividualBuilder().individualInitializer();

        Assertions.assertFalse(individual.getUuid().isEmpty());
        Assertions.assertFalse(individual.getIcp().isEmpty());
        Assertions.assertFalse(individual.getName().isEmpty());
        Assertions.assertFalse(individual.getSurname().isEmpty());
        Assertions.assertFalse(individual.getPatronymic().isEmpty());
        Assertions.assertFalse(individual.getFullName().isEmpty());
        Assertions.assertFalse(individual.getGender().name().isEmpty());
        Assertions.assertFalse(individual.getPlaceOfBirth().isEmpty());
        Assertions.assertFalse(individual.getCountryOfBirth().isEmpty());
        Assertions.assertFalse(individual.getBirthDate().toString().isEmpty());
        Assertions.assertFalse(individual.getDocuments().isEmpty());
        Assertions.assertFalse(individual.getWallet().isEmpty());
        Assertions.assertFalse(individual.getAvatar().isEmpty());
        Assertions.assertFalse(individual.getSavingsAccount().isEmpty());
        Assertions.assertFalse(individual.getContacts().isEmpty());
        Assertions.assertFalse(individual.getAddress().isEmpty());

    }
}