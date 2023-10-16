package org.kata.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.kata.entity.enums.GenderType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Table(name = "individual")
@Entity
@ToString
public class Individual {

    @Id
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "icp")
    private String icp;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Column(name = "place_of_birth")
    private String placeOfBirth;

    @Column(name = "country_of_birth")
    private String countryOfBirth;

    @Column(name = "birth_date")
    private Date birthDate;

    @OneToMany(mappedBy = "individual", cascade = CascadeType.ALL)
    private List<Document> documents;

    @OneToMany(mappedBy = "individual", cascade = CascadeType.ALL)
    private List<ContactMedium> contacts;

    @OneToMany(mappedBy = "individual", cascade = CascadeType.ALL)
    private List<Address> address;

    @OneToMany(mappedBy = "individual", cascade = CascadeType.ALL)
    private List<Avatar> avatar;

}


