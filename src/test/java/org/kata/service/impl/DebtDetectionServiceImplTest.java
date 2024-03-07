package org.kata.service.impl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.kata.entity.blackList.BlackListContacts;
import org.kata.entity.blackList.BlackListDocuments;
import org.kata.entity.blackList.BlackListIndividualBirthDate;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
public class DebtDetectionServiceImplTest {
    @Mock
    private BlackListDocuments mockDocuments;

    @Mock
    private BlackListIndividualBirthDate mockBirthDate;

    @Mock
    private BlackListContacts mockContacts;

    @InjectMocks
    private DebtDetectionServiceImpl debtDetectionService;

    @AfterAll
    static void infoTests() {
        System.out.println("Тесты завершены");
    }
    @AfterEach
    void infoTest() {
        System.out.println("Тест завершен");
    }




}
