package org.kata.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kata.controller.dto.IndividualDto;
import org.kata.controller.dto.WalletDto;
import org.kata.entity.Individual;
import org.kata.entity.Wallet;
import org.kata.exception.IndividualNotFoundException;
import org.kata.exception.WalletNotFoundException;
import org.kata.repository.IndividualCrudRepository;
import org.kata.repository.WalletCrudRepository;
import org.kata.service.impl.WalletServiceImpl;
import org.kata.service.mapper.WalletMapper;
import org.kata.util.FileUtil;
import org.kata.util.MapperUtil;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private IndividualCrudRepository individualCrudRepository;

    @Mock
    private WalletCrudRepository walletCrudRepository;

    @Mock
    private WalletMapper walletMapper;

    @InjectMocks
    private WalletServiceImpl walletService;

    private Individual individual;

    private IndividualDto individualDto;

    @BeforeEach
    void setUp() throws IOException {
        individual = MapperUtil.deserializeIndividual(FileUtil.readFromFileToString("TestIndividual2.json"));
        individualDto = MapperUtil.deserializeIndividualDto(FileUtil.readFromFileToString("TestIndividualDto2.json"));
    }

    @Test
    void testGetWalletWhenIndividualAndWalletExist() {
        when(individualCrudRepository.findByIcp(anyString())).thenReturn(Optional.of(individual));
        when(walletMapper.toDto(anyList())).thenReturn(individualDto.getWallet());
        List<WalletDto> resultWallets = walletService.getWallet("GOOD_ICP");
        assertEquals(resultWallets.get(0).getCurrencyType(), individual.getWallet().get(0).getCurrencyType());
        assertEquals(resultWallets.get(0).getBalance(), individual.getWallet().get(0).getBalance());
    }

    @Test
    void testGetWalletWhenIndividualNotExist() {
        assertThrows(IndividualNotFoundException.class, () -> walletService.getWallet("BAD_ICP"));
    }

    @Test
    void testGetWalletWhenWalletNotExist() {
        individual.setWallet(new ArrayList<>());
        when(individualCrudRepository.findByIcp(anyString())).thenReturn(Optional.of(individual));
        assertThrows(WalletNotFoundException.class, () -> walletService.getWallet(anyString()));
    }

    @Test
    void testSaveWalletWhenIndividualExist() {
        when(individualCrudRepository.findByIcp(anyString())).thenReturn(Optional.of(individual));
        when(walletMapper.toEntity(any(WalletDto.class))).thenReturn(individual.getWallet().get(0));
        when(walletCrudRepository.save(any())).thenReturn(individual.getWallet().get(0));
        when(walletMapper.toDto(any(Wallet.class))).thenReturn(individualDto.getWallet().get(0));
        assertNotNull(walletService.saveWallet(individualDto.getWallet().get(0)));
    }

    @Test
    void testSaveWalletWhenIndividualNotExist() {
        WalletDto badIcpDto = WalletDto.builder()
                .icp("BAD_ICP")
                .build();
        assertThrows(IndividualNotFoundException.class, () -> walletService.saveWallet(badIcpDto));
    }
}
