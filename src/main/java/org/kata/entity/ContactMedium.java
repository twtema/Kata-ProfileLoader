package org.kata.entity;

import lombok.Getter;
import lombok.Setter;
import org.kata.entity.enums.ContactMediumType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Table(name = "contact_medium")
@Entity
public class ContactMedium implements IndividualRelatedEntity {

    @Id
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ContactMediumType type;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "individual_uuid")
    private Individual individual;

    @Column(name = "is_actual")
    private boolean isActual;
}
