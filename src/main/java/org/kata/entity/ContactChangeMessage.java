package org.kata.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Entity of kafka message class which includes old & new contacts and confirmation code.
 * <p>
 * @author Aleksey Mischanchuk
 * */
@Data
@Entity
public class ContactChangeMessage {

    @Id
    private String icp;
    private String oldContactValue;
    private String newContactValue;
    private String confirmationCode;
}
