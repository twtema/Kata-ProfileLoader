package org.kata.mapper.setters;


import org.kata.controller.dto.IndividualDto;
import org.kata.entity.*;
import org.kata.entity.enums.GenderType;
import org.kata.service.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class IndividualSetter implements Setter {
    @Autowired
    @Qualifier("documentSetter")
    private Setter documentSetter;
    @Autowired
    @Qualifier("avatarSetter")
    private Setter avatarSetter;
    @Autowired
    @Qualifier("contactSetter")
    private Setter contactSetter;
    @Autowired
    @Qualifier("addressSetter")
    private Setter addressSetter;
    @Autowired
    @Qualifier("walletSetter")
    private Setter walletSetter;
    @Autowired
    AddressMapper addressMapper;
    @Autowired
    DocumentMapper documentMapper;
    @Autowired
    ContactMediumMapper contactMediumMapper;
    @Autowired
    AvatarMapper  avatarMapper;
    @Autowired
    WalletMapper walletMapper;

    @Override
    public void setEntityFields(Object individualObject) {
        Individual individual = (Individual) individualObject;
        StringFieldsSetterUtil.set(individualObject);
        individual.setAddress(generateEntityList(new Address(), addressSetter));
        individual.setDocuments(generateEntityList(new Document(), documentSetter));
        individual.setAvatar(generateEntityList(new Avatar(), avatarSetter));
        individual.setContacts(generateEntityList(new ContactMedium(), contactSetter));
        individual.setWallet(generateEntityList(new Wallet(), walletSetter));
        individual.setGender(GenderType.MALE);
        individual.setBirthDate(new Date());
    }

    @Override
    public void setDtoFields(Object individualDtoObject) {
        IndividualDto individualDto = (IndividualDto) individualDtoObject;
        StringFieldsSetterUtil.set(individualDto);
        individualDto.setGender(GenderType.MALE);
        individualDto.setAddress(generateDtoList(addressMapper.toDto(new Address()), addressSetter));
        individualDto.setDocuments(generateDtoList(documentMapper.toDto(new Document()), documentSetter));
        individualDto.setAvatar(generateDtoList(avatarMapper.toDto(new Avatar()), avatarSetter));
        individualDto.setContacts(generateDtoList(contactMediumMapper.toDto(new ContactMedium()), contactSetter));
        individualDto.setWallet(generateDtoList(walletMapper.toDto(new Wallet()), walletSetter));
        individualDto.setBirthDate(new Date());
    }

    private <T> List<T> generateDtoList(T object, Setter setter){
        return Stream.
                generate(new Random()::nextInt).
                limit(10)
                .map(i -> object)
                .peek(setter::setDtoFields)
                .toList();
    }
    private <T> List<T> generateEntityList(T object, Setter setter){
        return Stream.
                generate(new Random()::nextInt).
                limit(10)
                .map(i -> object)
                .peek(setter::setEntityFields)
                .toList();
    }
}
