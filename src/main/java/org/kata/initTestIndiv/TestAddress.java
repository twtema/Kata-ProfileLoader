package org.kata.initTestIndiv;

import lombok.Getter;
import lombok.Setter;
import org.kata.entity.Address;
import org.kata.entity.Individual;

import java.util.UUID;

@Getter
@Setter
public class TestAddress extends Address {
    private final String uuid = UUID.randomUUID().toString();
    private final String street = "Бауманская";
    private final String city = "Казань";
    private final String state = "Российская Федерация";
    private final String postCode = "45785";
    private final String country = "Россия";
    private Individual individual;
    private final boolean isActual = true;
}
