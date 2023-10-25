package org.kata.mapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kata.controller.dto.ContactMediumDto;
import org.kata.entity.ContactMedium;
import org.kata.entity.enums.ContactMediumType;
import org.kata.mapper.util.MapperChecker;
import org.kata.service.mapper.ContactMediumMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ContactMediumMapperTest implements MapperTest<ContactMedium, ContactMediumDto>{
    @Autowired
    private ContactMediumMapper contactMediumMapper;
    @Autowired
    private MapperChecker mapperChecker;

    private ContactMedium contactMedium;
    private ContactMediumDto contactMediumDto;

    @Before
    public void setUp() {
        contactMedium = new ContactMedium();
        contactMediumDto = contactMediumMapper.toDto(contactMedium);
        setEntityFields(contactMedium);
        setDtoFields(contactMediumDto);
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


    @Override
    public void setEntityFields(ContactMedium contactMedium) {
        contactMedium.setType(ContactMediumType.PHONE);
        contactMedium.setValue("FromEntityValue");
    }

    @Override
    public void setDtoFields(ContactMediumDto contactMediumDto) {
        contactMediumDto.setType(ContactMediumType.EMAIL);
        contactMediumDto.setValue("FromDtoValue");
    }
}
