package org.kata.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.kata.entity.enums.CurrencyType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

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

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
    private List<BankCard> bankCard;
}
