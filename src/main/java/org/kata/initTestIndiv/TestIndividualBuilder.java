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
        TestAccount testWallet = new TestAccount();

        testIndividual.setAddress(Collections.singletonList(testAddress));
        testIndividual.setAvatar(Collections.singletonList(testAvatar));
        testIndividual.setContacts(Collections.singletonList(testContacts));
        testIndividual.setDocuments(Collections.singletonList(testDocument));
        testIndividual.setAccount(Collections.singletonList(testWallet));

        testAddress.setIndividual(testIndividual);
        testAvatar.setIndividual(testIndividual);
        testContacts.setIndividual(testIndividual);
        testDocument.setIndividual(testIndividual);
        testWallet.setIndividual(testIndividual);

        return testIndividual;
    }
}
