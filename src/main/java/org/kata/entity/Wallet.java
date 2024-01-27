package org.kata.entity;

import lombok.Getter;
import lombok.Setter;
import org.kata.entity.enums.CurrencyType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@Table(name = "wallet")
@Entity
public class Wallet implements IndividualRelatedEntity{

    @Id
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "wallet_id")
    private String walletId;

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
