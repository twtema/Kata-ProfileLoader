package org.kata.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kata.controller.dto.ContactMediumDto;
import org.kata.controller.dto.DocumentDto;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.blackList.BlackListContacts;
import org.kata.entity.blackList.BlackListDocuments;
import org.kata.entity.blackList.BlackListIndividualBirthDate;
import org.kata.exception.TerroristDetectedException;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

class TerroristDetectionServiceImplTest {

    //Создаем объект класса TerroristDetectionServiceImpl и объекты его полей из blackLists
    private final BlackListDocuments blackListDocuments = new BlackListDocuments();
    private final BlackListContacts blackListContacts = new BlackListContacts();
    private final BlackListIndividualBirthDate blackListIndividualBirthDate = new BlackListIndividualBirthDate();

    private final TerroristDetectionServiceImpl terroristDetectionService =
            new TerroristDetectionServiceImpl(blackListDocuments, blackListContacts, blackListIndividualBirthDate);


    /*Тест проверяет метод isBlackListDocument(), метод возвращает true, если у индивидуала серия и номер документа
    совпадает серией и номером документа из BlackLists*/
    @Test
    void isBlackListDocumentReturnTrue() {
        //создаем рандомный documentDto
        DocumentDto documentDto = DocumentDto.builder().build();
        documentDto.setDocumentSerial("123");
        documentDto.setDocumentNumber("456789");

        //создаем рандомный individualDto и присваиваем ему созданный документ
        IndividualDto dto = IndividualDto.builder().build();
        dto.setDocuments(Collections.singletonList(documentDto));

        //устанавливаем рандоиные значения для blackList
        blackListDocuments.setSeries(Collections.singletonList("123"));
        blackListDocuments.setNumbers(Collections.singletonList("456789"));

        //вызываем сам метод и передаем туда созданный dto
        Assertions.assertTrue(terroristDetectionService.isBlackListDocument(dto));
    }

    /*Тест проверяет метод isBlackListContacts(), метод возвращает true, если у индивидуала номер совпадает
    с номером из BlackLists*/
    @Test
    void isBlackListContactsReturnTrue() {
        ContactMediumDto contactMediumDto = ContactMediumDto.builder().build();
        contactMediumDto.setValue("89008005050");

        IndividualDto dto = IndividualDto.builder().build();
        dto.setContacts(Collections.singletonList(contactMediumDto));

        blackListContacts.setNumbervalue(Collections.singletonList("89008005050"));

        Assertions.assertTrue(terroristDetectionService.isBlackListContacts(dto));
    }

    /*Тест проверяет метод isBlackListBirthDate(), метод возвращает true, если у индивидуала дата рождения
    совпадает с датой из BlackList */
    @Test
    void isBlackListBirthDateReturnTrue() {
        IndividualDto dto = IndividualDto.builder().build();
        dto.setBirthDate(new Date(95, Calendar.MAY, 12));

        blackListIndividualBirthDate.setBirthDate(LocalDate.of(1995, 5, 12));

        Assertions.assertTrue(terroristDetectionService.isBlackListBirthDate(dto));
    }

    //Тест проверяет индивидуала и выбрасывает исключение если все поля совпадают с полями из blackList
    @Test
    void checkIndividualReturnException() {
        DocumentDto documentDto = DocumentDto.builder().build();
        documentDto.setDocumentSerial("123");
        documentDto.setDocumentNumber("456789");

        ContactMediumDto contactMediumDto = ContactMediumDto.builder().build();
        contactMediumDto.setValue("89008005050");

        IndividualDto dto = IndividualDto.builder().build();
        dto.setDocuments(Collections.singletonList(documentDto));
        dto.setContacts(Collections.singletonList(contactMediumDto));
        dto.setBirthDate(new Date(95, Calendar.MAY, 12));

        blackListDocuments.setSeries(Collections.singletonList("123"));
        blackListDocuments.setNumbers(Collections.singletonList("456789"));
        blackListContacts.setNumbervalue(Collections.singletonList("89008005050"));
        blackListIndividualBirthDate.setBirthDate(LocalDate.of(1995, 5, 12));

        Assertions.assertThrows(TerroristDetectedException.class, () -> {
            terroristDetectionService.checkIndividual(dto);
        });
    }

    //Тест проверят присваивается ли полю UnwantedCustomer значение true если одно из полей совпадает с blackList
    @Test
    void checkIndividualEditUnwantedCustomer() {
        DocumentDto documentDto = DocumentDto.builder().build();
        documentDto.setDocumentSerial("123");
        documentDto.setDocumentNumber("456789");

        ContactMediumDto contactMediumDto = ContactMediumDto.builder().build();
        contactMediumDto.setValue("89008005050");

        IndividualDto dto = IndividualDto.builder().build();
        dto.setDocuments(Collections.singletonList(documentDto));
        dto.setContacts(Collections.singletonList(contactMediumDto));
        dto.setBirthDate(new Date(95, Calendar.MAY, 12));

        blackListDocuments.setSeries(Collections.singletonList("133"));
        blackListDocuments.setNumbers(Collections.singletonList("457789"));
        blackListContacts.setNumbervalue(Collections.singletonList("89708005050"));
        blackListIndividualBirthDate.setBirthDate(LocalDate.of(1995, 5, 12));

        terroristDetectionService.checkIndividual(dto);

        Assertions.assertTrue(dto.isUnwantedCustomer());
    }
}