package org.kata.entity;

import lombok.Getter;
import lombok.Setter;
import org.kata.entity.enums.Currency;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Table(name = "wallet")
@Entity
public class Wallet {

    @Id
    @Column(name = "uuid")
    private String uuid;

    @OneToOne
    @JoinColumn(name = "individual_uuid")
    private Individual individual;

    @Column(name = "currency")
    private Currency currency;

    @Column(name = "value")
    private BigDecimal value;

}
