package org.kata.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Objects;

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

    @Column(name = "hex")
    private String hex;

    @Lob
    @Type(type="org.hibernate.type.ImageType")
    @Column(name = "image_data")
    private byte[] imageData;

    @Column(name = "upload_date")
    private ZonedDateTime uploadDate;

    @ManyToOne
    @JoinColumn(name = "individual_uuid")
    private Individual individual;

    @Column(name = "is_actual")
    private boolean isActual;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Avatar avatar = (Avatar) o;
        return Objects.equals(hex, avatar.hex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hex);
    }
}
