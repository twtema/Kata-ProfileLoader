package org.kata.mapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kata.controller.dto.BankCardDto;
import org.kata.entity.BankCard;
import org.kata.mapper.setters.Setter;
import org.kata.mapper.util.MapperChecker;
import org.kata.service.mapper.BankCardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class BankCardMapperTest implements MapperTest<BankCard, BankCardDto> {
    @Autowired
    private BankCardMapper bankCardMapper;
    @Autowired
    private MapperChecker mapperChecker;
    @Autowired
    @Qualifier("bankCardSetter")
    private Setter setter;
    private BankCard bankCardFrom;
    private BankCardDto bankCardDtoFrom;

    @Before
    public void setUp() {
        bankCardFrom = new BankCard();
        bankCardDtoFrom = bankCardMapper.toDto(bankCardFrom);
        setter.setEntityFields(bankCardFrom);
        setter.setDtoFields(bankCardDtoFrom);
    }
    @Test
    @Override
    public void shouldMapEntityToDto() throws IllegalAccessException {
        mapperChecker.checkFieldsEquivalence(
                bankCardFrom,
                bankCardMapper.toDto(bankCardFrom));
    }
    @Test
    @Override
    public void shouldMapDtoToEntity() throws IllegalAccessException {
        mapperChecker.checkFieldsEquivalence(
                bankCardFrom,
                bankCardMapper.toEntity(bankCardDtoFrom));
    }
}
