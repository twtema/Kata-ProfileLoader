package org.kata.mapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kata.controller.dto.WalletDto;
import org.kata.entity.Wallet;
import org.kata.mapper.setters.Setter;
import org.kata.mapper.util.MapperChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class WalletMapperTest implements MapperTest <Wallet, WalletDto>{
    @Autowired
    private WalletMapper walletMapper;
    @Autowired
    private MapperChecker mapperChecker;
    @Autowired
    @Qualifier("walletSetter")
    private Setter setter;

    private Wallet walletFrom;
    private WalletDto walletDtoFrom;

    @Before
    public void setUp() {
        walletFrom = new Wallet();
        walletDtoFrom = walletMapper.toDto(walletFrom);
        setter.setEntityFields(walletFrom);
        setter.setDtoFields(walletDtoFrom);
    }
    @Override
    @Test
    public void shouldMapEntityToDto() throws IllegalAccessException {
        mapperChecker.checkFieldsEquivalence(
                walletFrom,
                walletMapper.toDto(walletFrom));
    }
    @Override
    @Test
    public void shouldMapDtoToEntity() throws IllegalAccessException {
        mapperChecker.checkFieldsEquivalence(
                walletDtoFrom,
                walletMapper.toEntity(walletDtoFrom));
    }
}
