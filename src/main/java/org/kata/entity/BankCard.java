package org.kata.entity;

import lombok.*;
import org.kata.entity.enums.BankCardType;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@Table(name = "bank_card")
@Entity
public class BankCard implements IndividualRelatedEntity {
    @Id
    @Column(name = "uuid")
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "individual_uuid")
    private Individual individual;

    @ManyToOne
    @JoinColumn(name = "account_uuid")
    private Account account;

    @Column(name = "holder_name")
    private String holderName;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "bank_card_type")
    @Enumerated(EnumType.STRING)
    private BankCardType bankCardType;

    @Column(name = "cvv")
    private Integer cvv;

    @Column(name = "is_actual")
    private boolean isActual;

}
