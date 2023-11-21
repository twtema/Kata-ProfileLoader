package org.kata.entity;

import lombok.Getter;
import lombok.Setter;
import org.kata.entity.enums.Currency;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;


@Getter
@Setter
@Entity
@Table(name = "savings_account")
public class SavingsAccount {
    @Id
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "currency")
    private Currency currency;


    @Column(name = "info_Of_Percent")
    private String infoOfPercent;

    @Column(name = "final_sum")
    private BigDecimal finalSum;


    @Column(name = "deposit_term")
    private Short depositTerm;

    @ManyToOne
    @JoinColumn(name = "individual_uuid")
    private Individual individual;

}
