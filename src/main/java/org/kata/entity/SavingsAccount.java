package org.kata.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Getter
@Setter
@Entity
@Table(name = "savings_account")
public class SavingsAccount {
    @Id
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "currency")
    private String currency;


    @Column(name = "info_Of_Percent")
    private String infoOfPercent;

    @Column(name = "final_sum")
    private Long finalSum;


    @Column(name = "deposit_term")
    private Byte depositTerm;

    @ManyToOne
    @JoinColumn(name = "individual_uuid")
    private Individual individual;

}
