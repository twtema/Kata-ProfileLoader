package org.kata.entity;

import lombok.Getter;
import lombok.Setter;
import org.kata.entity.enums.BlackListDocumentType;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "blacklist_document")
@Entity
public class BlackListDocument {
    @Id
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "document_type")
    @Enumerated(EnumType.STRING)
    private BlackListDocumentType blackListDocumentType;

    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "document_serial")
    private String documentSerial;

    @Column(name = "is_actual")
    private boolean isActual;
}

