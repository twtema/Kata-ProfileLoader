package org.kata.mapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.*;
import org.kata.entity.enums.GenderType;
import org.kata.mapper.setters.Setter;
import org.kata.mapper.util.MapperChecker;
import org.kata.service.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Autowired
    @Qualifier("individualSetter")
    private Setter setter;

    private Individual individualFrom;
    private IndividualDto individualDtoFrom;

    @Before
    public void setUp() {
        individualFrom = new Individual();
        individualDtoFrom = individualMapper.toDto(individualFrom);
        setter.setEntityFields(individualFrom);
        setter.setDtoFields(individualDtoFrom);
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
}
