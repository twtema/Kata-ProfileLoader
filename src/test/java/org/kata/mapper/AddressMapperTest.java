package org.kata.mapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kata.controller.dto.AddressDto;
import org.kata.entity.Address;
import org.kata.mapper.setters.Setter;
import org.kata.mapper.util.MapperChecker;
import org.kata.service.mapper.AddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class AddressMapperTest implements MapperTest<Address, AddressDto>{
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private MapperChecker mapperChecker;
    @Autowired
    @Qualifier("addressSetter")
    private Setter setter;

    private Address addressFrom;
    private AddressDto addressDtoFrom;

    @Before
    public void setUp() {
        addressFrom = new Address();
        addressDtoFrom = addressMapper.toDto(addressFrom);
        setter.setEntityFields(addressFrom);
        setter.setDtoFields(addressDtoFrom);
    }
    @Override
    @Test
    public void shouldMapEntityToDto() throws IllegalAccessException {
        mapperChecker.checkFieldsEquivalence(
                addressFrom,
                addressMapper.toDto(addressFrom));
    }
    @Override
    @Test
    public void shouldMapDtoToEntity() throws IllegalAccessException {
        mapperChecker.checkFieldsEquivalence(
                addressDtoFrom,
                addressMapper.toEntity(addressDtoFrom));
    }
}
