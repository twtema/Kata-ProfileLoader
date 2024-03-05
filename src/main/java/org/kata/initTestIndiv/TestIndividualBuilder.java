package org.kata.initTestIndiv;

import org.kata.entity.Individual;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class TestIndividualBuilder extends Individual {

    public Individual individualInitializer() {
        TestIndividual testIndividual = new TestIndividual();
        TestAddress testAddress = new TestAddress();
        TestAvatar testAvatar = new TestAvatar();
        TestContacts testContacts = new TestContacts();
        TestDocument testDocument = new TestDocument();
        TestWallet testWallet = new TestWallet();
        TestSavingsAccount testSavingsAccount = new TestSavingsAccount();

        testIndividual.setAddress(Collections.singletonList(testAddress));
        testIndividual.setAvatar(Collections.singletonList(testAvatar));
        testIndividual.setContacts(Collections.singletonList(testContacts));
        testIndividual.setDocuments(Collections.singletonList(testDocument));
        testIndividual.setWallet(Collections.singletonList(testWallet));
        testIndividual.setSavingsAccount(Collections.singletonList(testSavingsAccount));

        testAddress.setIndividual(testIndividual);
        testAvatar.setIndividual(testIndividual);
        testContacts.setIndividual(testIndividual);
        testDocument.setIndividual(testIndividual);
        testWallet.setIndividual(testIndividual);
        testSavingsAccount.setIndividual(testIndividual);

        return testIndividual;
    }
}
