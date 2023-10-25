package org.kata.mapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.*;
import org.kata.entity.enums.GenderType;
import org.kata.mapper.util.MapperChecker;
import org.kata.service.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
public class IndividualMapperTest implements MapperTest<Individual, IndividualDto> {
    @Autowired
    private IndividualMapper individualMapper;
    @Autowired
    private MapperChecker mapperChecker;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private ContactMediumMapper contactMediumMapper;
    @Autowired
    private DocumentMapper documentMapper;
    @Autowired
    private AvatarMapper avatarMapper;

    private Individual individualFrom;
    private IndividualDto individualDtoFrom;

    @Before
    public void setUp() {
        individualFrom = new Individual();
        individualDtoFrom = individualMapper.toDto(individualFrom);
        setEntityFields(individualFrom);
        setDtoFields(individualDtoFrom);
    }

    @Override
    @Test
    public void shouldMapEntityToDto() throws IllegalAccessException {
        mapperChecker.checkFieldsEquivalence(
                individualFrom,
                individualMapper.toDto(individualFrom));
    }

    @Override
    @Test
    public void shouldMapDtoToEntity() throws IllegalAccessException {
        mapperChecker.checkFieldsEquivalence(
                individualDtoFrom,
                individualMapper.toEntity(individualDtoFrom));
    }


    @Override
    public void setEntityFields(Individual individual) {
        individual.setPlaceOfBirth("placeOfBirth");
        List<Address> addresses = new ArrayList<>();
        Address address = new Address();
        Individual individual1 = new Individual();
        individual.setGender(GenderType.MALE);
        address.setIndividual(individual1);
        addresses.add(address);
        addresses.add(new Address());
        individual.setAddress(addresses);
        individual.setGender(GenderType.MALE);
        individual.setDocuments(new ArrayList<>());
        individual.setFullName("FromEntityFullName");
        individual.setAvatar(new ArrayList<>());
        individual.setBirthDate(new Date());
        individual.setPatronymic("FromEntityPatronymic");
        individual.setCountryOfBirth("FromEntityCountryOfBirth");
        individual.setSurname("FromEntitySurname");
        individual.setIcp("FromEntityIcp");
        individual.setName("FromEntityName");
        individual.setContacts(new ArrayList<>());
    }

    @Override
    public void setDtoFields(IndividualDto individualDto) {
        individualDto.setPlaceOfBirth("placeOfBirth");
        individualDto.setAddress(new ArrayList<>());
        individualDto.setGender(GenderType.MALE);
        individualDto.setDocuments(new ArrayList<>());
        individualDto.setFullName("FromEntityFullName");
        individualDto.setAvatar(new ArrayList<>());
        individualDto.setBirthDate(new Date());
        individualDto.setPatronymic("FromEntityPatronymic");
        individualDto.setCountryOfBirth("FromEntityCountryOfBirth");
        individualDto.setSurname("FromEntitySurname");
        individualDto.setIcp("FromEntityIcp");
        individualDto.setName("FromEntityName");
        individualDto.setContacts(new ArrayList<>());
    }
}
