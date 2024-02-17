package org.kata.staticObjects;

import org.kata.entity.*;
import org.kata.entity.enums.*;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

//класс для генерации тестового пользователя
@Component
public class ForTestIndividual extends Individual {
//Генерация индивидуала
    Individual testIndividual = new Individual();

//Установка Icp и Uuid
    public Individual createIndividual() {
        testIndividual.setUuid("100");
        testIndividual.setIcp("100");
        return testIndividual;
    }

//Генерация документа
    public List<Document> getTestDocument() {
        Document testDocument = new Document();
        testDocument.setUuid("100");
        testDocument.setDocumentType(DocumentType.RF_PASSPORT);
        testDocument.setActual(true);
        testDocument.setDocumentNumber("553869");
        testDocument.setDocumentSerial("1121");
        testDocument.setIndividual(createIndividual());
        testDocument.setIssueDate(new Date(118, Calendar.APRIL, 10, 0, 0));
        testDocument.setExpirationDate(new Date(134, Calendar.APRIL, 10, 0, 0));

        return Collections.singletonList(testDocument);
    }
//Генерация контактов
    public List<ContactMedium> getTestContact() {
        ContactMedium testContact = new ContactMedium();
        testContact.setUuid("100");
        testContact.setIndividual(createIndividual());
        testContact.setActual(true);
        testContact.setType(ContactMediumType.PHONE);
        testContact.setValue("+7(900)-140-25-13");
        return Collections.singletonList(testContact);
    }
//Генерация адреса
    public List<Address> getTestAddress() {
        Address testAddress = new Address();
        testAddress.setUuid("100");
        testAddress.setIndividual(createIndividual());
        testAddress.setActual(true);
        testAddress.setCity("Казань");
        testAddress.setState("Российская Федерация");
        testAddress.setCountry("Россия");
        testAddress.setStreet("Бауманская");
        testAddress.setPostCode("45785");
        return Collections.singletonList(testAddress);
    }
//Генерация аватара
    public List<Avatar> getTestAvatar() throws IOException {
        File file = new File("src\\main\\resources\\static\\avatarTestIndividual.jpg");
        InputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        byte[] image = byteArrayOutputStream.toByteArray();
        inputStream.close();
        byteArrayOutputStream.close();

        Avatar testAvatar = new Avatar();
        testAvatar.setIndividual(createIndividual());
        testAvatar.setImageData(image);
        testAvatar.setFilename("avatarTestIndividual.jpg");
        testAvatar.setActual(true);
        return Collections.singletonList(testAvatar);
    }
//Генерация платежки
    public List<Wallet> getTestWallet() {
        Wallet testWsllet = new Wallet();
        testWsllet.setUuid("100");
        testWsllet.setWalletId("100");
        testWsllet.setIndividual(createIndividual());
        testWsllet.setActual(true);
        testWsllet.setBalance(BigDecimal.valueOf(50000));
        testWsllet.setCurrencyType(CurrencyType.RUB);
        return Collections.singletonList(testWsllet);
    }

    public List<SavingsAccount> getTestSavingAccount() {
        SavingsAccount testAccount = new SavingsAccount();
        testAccount.setUuid("100");
        testAccount.setIndividual(createIndividual());
        testAccount.setInfoOfPercent("noInfo");
        testAccount.setCurrency(Currency.RUB);
        testAccount.setFinalSum(BigDecimal.valueOf(150000));
        testAccount.setDepositTerm((short) 15);
        return Collections.singletonList(testAccount);
    }
//Заполнение данных индивидуала
    public Individual getTestIndividual() {
        Individual testIndividual = createIndividual();
        testIndividual.setName("Петр");
        testIndividual.setSurname("Давыдов");
        testIndividual.setPatronymic("Иванович");
        testIndividual.setFullName("Давыдов Петр Иванович");
        testIndividual.setGender(GenderType.MALE);
        testIndividual.setPlaceOfBirth("Казань");
        testIndividual.setCountryOfBirth("Россия");
        testIndividual.setBirthDate(new Date(92, Calendar.NOVEMBER, 15, 0, 0));
        testIndividual.setDocuments(getTestDocument());
        testIndividual.setContacts(getTestContact());
        testIndividual.setAddress(getTestAddress());
        try {
            testIndividual.setAvatar(getTestAvatar());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        testIndividual.setWallet(getTestWallet());
        testIndividual.setSavingsAccount(getTestSavingAccount());
        return testIndividual;
    }
}
