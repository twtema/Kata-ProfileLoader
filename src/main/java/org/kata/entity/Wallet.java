package org.kata.entity;

import lombok.Getter;
import lombok.Setter;
import org.kata.entity.enums.CurrencyType;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Table(name = "wallet")
@Entity
public class Wallet implements IndividualRelatedEntity{

    @Id
    @Column(name = "uuid")
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "individual_uuid")
    private Individual individual;

    @Column(name = "currency_type")
    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    @Column(name = "balance")
    private BigDecimal balance;

}
