package org.kata.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Table(name = "bank_card")
@Entity
@ToString
public class BankCard implements IndividualRelatedEntity {
    @Id
    @Column(name = "uuid")
    private String uuid;

//    @ManyToOne
//    @JoinColumn(name = "wallet_uuid")
//    private Wallet wallet;

    @ManyToOne
    @JoinColumn(name = "individual_uuid")
    private Individual individual;

    @Column(name = "holder_name")
    private String holderName;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "cvv")
    private int cvv;

    @Column(name = "is_actual")
    private boolean isActual;

}
