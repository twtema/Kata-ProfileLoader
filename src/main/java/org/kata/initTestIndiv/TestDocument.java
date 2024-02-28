package org.kata.initTestIndiv;

import lombok.Getter;
import lombok.Setter;
import org.kata.entity.Document;
import org.kata.entity.Individual;
import org.kata.entity.enums.DocumentType;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class TestDocument extends Document {
    private final String uuid = UUID.randomUUID().toString();
    private final DocumentType documentType = DocumentType.RF_PASSPORT;
    private final String documentNumber = "510466";
    private final String documentSerial = "2010";
    private final Date issueDate = new Date(118, Calendar.APRIL, 10, 0, 0);
    private final Date expirationDate = new Date(134, Calendar.APRIL, 10, 0, 0);
    private Individual individual;
    private final boolean isActual = true;
}
