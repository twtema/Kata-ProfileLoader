package org.kata.mapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kata.controller.dto.ContactMediumDto;
import org.kata.entity.ContactMedium;
import org.kata.mapper.setters.Setter;
import org.kata.mapper.util.MapperChecker;
import org.kata.service.mapper.ContactMediumMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ContactMediumMapperTest implements MapperTest<ContactMedium, ContactMediumDto>{
    @Autowired
    private ContactMediumMapper contactMediumMapper;
    @Autowired
    private MapperChecker mapperChecker;
    @Autowired
    @Qualifier("contactSetter")
    private Setter setter;

    private ContactMedium contactMedium;
    private ContactMediumDto contactMediumDto;

    @Before
    public void setUp() {
        contactMedium = new ContactMedium();
        contactMediumDto = contactMediumMapper.toDto(contactMedium);
        setter.setEntityFields(contactMedium);
        setter.setDtoFields(contactMediumDto);
    }
    @Override
    @Test
    public void shouldMapEntityToDto() throws IllegalAccessException {
        mapperChecker.checkFieldsEquivalence(
                contactMedium,
                contactMediumMapper.toDto(contactMedium));
    }
    @Override
    @Test
    public void shouldMapDtoToEntity() throws IllegalAccessException {
        mapperChecker.checkFieldsEquivalence(
                contactMediumDto,
                contactMediumMapper.toEntity(contactMediumDto));
    }



}
