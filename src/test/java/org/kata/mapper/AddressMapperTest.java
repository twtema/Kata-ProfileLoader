package org.kata.mapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kata.controller.dto.AddressDto;
import org.kata.entity.Address;
import org.kata.service.mapper.AddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class AddressMapperTest implements MapperTest<Address, AddressDto>{
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private MapperChecker mapperChecker;

    private Address addressFrom;
    private AddressDto addressDtoFrom;

    @Before
    public void setUp() {
        addressFrom = new Address();
        addressDtoFrom = addressMapper.toDto(addressFrom);
        setEntityFields(addressFrom);
        setDtoFields(addressDtoFrom);
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


    @Override
    public void setEntityFields(Address address) {
        address.setPostCode("FromEntityPostCode");
        address.setCity("FromEntityCity");
        address.setCountry("FromEntityCountry");
        address.setState("FromEntityState");
        address.setStreet("FromEntityStreet");
    }

    @Override
    public void setDtoFields(AddressDto addressDto) {
        addressDto.setPostCode("FromDtoPostCode");
        addressDto.setCity("FromDtoCity");
        addressDto.setCountry("FromDtoCountry");
        addressDto.setState("FromDtoState");
        addressDto.setStreet("FromDtoStreet");
    }
}
