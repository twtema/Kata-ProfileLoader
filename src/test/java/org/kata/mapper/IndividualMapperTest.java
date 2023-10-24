package org.kata.mapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kata.controller.dto.AddressDto;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.*;
import org.kata.entity.enums.GenderType;
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
        individual.setAddress(List.of(new Address(), new Address()));
        individual.setGender(GenderType.MALE);
        individual.setDocuments(List.of(new Document(), new Document()));
        individual.setFullName("FromEntityFullName");
        individual.setAvatar(List.of(new Avatar(), new Avatar()));
        individual.setBirthDate(new Date());
        individual.setPatronymic("FromEntityPatronymic");
        individual.setCountryOfBirth("FromEntityCountryOfBirth");
        individual.setSurname("FromEntitySurname");
        individual.setIcp("FromEntityIcp");
        individual.setName("FromEntityName");
        individual.setContacts(List.of(new ContactMedium(), new ContactMedium()));
    }

    @Override
    public void setDtoFields(IndividualDto individualDto) {
        individualDto.setPlaceOfBirth("placeOfBirth");
        List<Address> addresses = new ArrayList<>();
        addresses.add(new Address());
        addresses.add(new Address());
        List<AddressDto> addressDtoList = addresses.stream().map(address -> addressMapper.toDto(address)).toList();
        individualDto.setAddress(addressDtoList);
        individualDto.setGender(GenderType.MALE);
        individualDto.setDocuments(List.of(documentMapper.toDto(new Document()), documentMapper.toDto(new Document())));
        individualDto.setFullName("FromEntityFullName");
        individualDto.setAvatar(List.of(avatarMapper.toDto(new Avatar()), avatarMapper.toDto(new Avatar())));
        individualDto.setBirthDate(new Date());
        individualDto.setPatronymic("FromEntityPatronymic");
        individualDto.setCountryOfBirth("FromEntityCountryOfBirth");
        individualDto.setSurname("FromEntitySurname");
        individualDto.setIcp("FromEntityIcp");
        individualDto.setName("FromEntityName");
        individualDto.setContacts(List.of(contactMediumMapper.toDto(new ContactMedium()), contactMediumMapper.toDto(new ContactMedium())));
    }
}
