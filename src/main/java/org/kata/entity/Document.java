package org.kata.entity;

import lombok.Getter;
import lombok.Setter;
import org.kata.entity.enums.DocumentType;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Table(name = "document")
@Entity
public class Document implements IndividualRelatedEntity {

    @Id
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "document_type")
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "document_serial")
    private String documentSerial;

    @Column(name = "issue_date")
    private Date issueDate;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @ManyToOne
    @JoinColumn(name = "individual_uuid")
    private Individual individual;

    @Column(name = "is_actual")
    private boolean isActual;

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public void setIndividual(Individual individual) {
        this.individual = individual;
    }
}
