package org.kata.initTestIndiv;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kata.entity.Document;
import org.kata.entity.Individual;


class TestIndividualBuilderTest {

    //Тест на проверку пустоты объектов связанных с индувидиулом
    @Test
    void testIndividualInitializerReturnsNonEmptyLists() {
        TestIndividual testIndividual = new TestIndividual();
        TestAddress testAddress = new TestAddress();
        TestAvatar testAvatar = new TestAvatar();
        TestContacts testContacts = new TestContacts();
        TestDocument testDocument = new TestDocument();
        TestWallet testWallet = new TestWallet();
        TestSavingsAccount testSavingsAccount = new TestSavingsAccount();

        Assertions.assertNotNull(testIndividual);
        Assertions.assertNotNull(testAddress);
        Assertions.assertNotNull(testAvatar);
        Assertions.assertNotNull(testContacts);
        Assertions.assertNotNull(testDocument);
        Assertions.assertNotNull(testWallet);
        Assertions.assertNotNull(testSavingsAccount);

    }

    //Тест на то, что метод individualInitializer() возвращает Individual
    @Test
    void testRelationshipBetweenIndividualAndOtherObjects() {
        Individual individual = new TestIndividualBuilder().individualInitializer();
        Assertions.assertNotNull(individual);
        Assertions.assertInstanceOf(Individual.class, individual);
    }

    //Тест на создание индивидуала с заполненными полями при создании объекта
    @Test
    void builderTestIndividual() {
        TestIndividual testIndividual = new TestIndividual();

        Assertions.assertFalse(testIndividual.getIcp().isEmpty());
        Assertions.assertFalse(testIndividual.getName().isEmpty());
        Assertions.assertFalse(testIndividual.getSurname().isEmpty());
        Assertions.assertFalse(testIndividual.getFullName().isEmpty());
        Assertions.assertFalse(testIndividual.getCountryOfBirth().isEmpty());
        Assertions.assertFalse(testIndividual.getGender().toString().isEmpty());
        Assertions.assertFalse(testIndividual.getPatronymic().isEmpty());
        Assertions.assertFalse(testIndividual.getPlaceOfBirth().isEmpty());
        Assertions.assertFalse(testIndividual.getUuid().isEmpty());
        Assertions.assertFalse(testIndividual.getBirthDate().toString().isEmpty());
    }

    //Тест на создание связывание индивидуала с остальными объектами
    @Test
    void individualInitializerNotIsEmpti() {

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