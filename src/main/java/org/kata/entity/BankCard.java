package org.kata.entity;

import lombok.*;
import org.kata.entity.enums.BankCardType;


import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
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
    @Size(min = 16, max = 16, message = "Номер карты должна содержать 16 символов")
    private String cardNumber;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "bank_card_type")
    @Enumerated(EnumType.STRING)
    private BankCardType bankCardType;

    @Column(name = "cvv")
    @Digits(integer = 3, fraction = 0, message = "CVV код должен содержать 3 цифры")
    private int cvv;

    @Column(name = "is_actual")
    private boolean isActual;

}
