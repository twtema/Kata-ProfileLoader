package org.kata.mapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kata.controller.dto.AccountDto;
import org.kata.entity.Account;
import org.kata.mapper.setters.Setter;
import org.kata.mapper.util.MapperChecker;
import org.kata.service.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class AccountMapperTest implements MapperTest <Account, AccountDto>{
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private MapperChecker mapperChecker;
    @Autowired
    @Qualifier("accountSetter")
    private Setter setter;

    private Account accountFrom;
    private AccountDto accountDtoFrom;

    @Before
    public void setUp() {
        accountFrom = new Account();
        accountDtoFrom = accountMapper.toDto(accountFrom);
        setter.setEntityFields(accountFrom);
        setter.setDtoFields(accountDtoFrom);
    }
    @Override
    @Test
    public void shouldMapEntityToDto() throws IllegalAccessException {
        mapperChecker.checkFieldsEquivalence(
                accountFrom,
                accountMapper.toDto(accountFrom));
    }
    @Override
    @Test
    public void shouldMapDtoToEntity() throws IllegalAccessException {
        mapperChecker.checkFieldsEquivalence(
                accountDtoFrom,
                accountMapper.toEntity(accountDtoFrom));
    }
}