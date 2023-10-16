package org.kata.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "avatar")
public class Avatar implements IndividualRelatedEntity {

    @Id
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "filename")
    private String filename;

    @Lob
    @Column(name = "image_data", columnDefinition = "BLOB")
    private byte[] imageData;

    @ManyToOne
    @JoinColumn(name = "individual_uuid")
    private Individual individual;

    @Column(name = "is_actual")
    private boolean isActual;
}
