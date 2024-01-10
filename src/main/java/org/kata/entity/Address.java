package org.kata.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "address")
@Entity
public class Address implements IndividualRelatedEntity {

    @Id
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "post_code")
    private String postCode;

    @Column(name = "country")
    private String country;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "individual_uuid")
    private Individual individual;

    @Column(name = "is_actual")
    private boolean isActual;
}
