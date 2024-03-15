package org.kata.entity;

import lombok.Getter;
import lombok.Setter;
import org.kata.entity.enums.CurrencyType;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Table(name = "account")
@Entity
public class Account implements IndividualRelatedEntity{

    @Id
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "account_id")
    private String accountId;

    @ManyToOne
    @JoinColumn(name = "individual_uuid")
    private Individual individual;

    @Column(name = "currency_type")
    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "is_actual")
    private boolean isActual;

}
