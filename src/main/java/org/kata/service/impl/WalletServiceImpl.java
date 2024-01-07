package org.kata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.DocumentDto;
import org.kata.controller.dto.WalletDto;
import org.kata.entity.Document;
import org.kata.entity.Individual;
import org.kata.entity.Wallet;
import org.kata.exception.IndividualNotFoundException;
import org.kata.exception.WalletNotFoundException;
import org.kata.repository.IndividualCrudRepository;
import org.kata.repository.WalletCrudRepository;
import org.kata.service.WalletService;
import org.kata.service.mapper.WalletMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletCrudRepository walletCrudRepository;

    private final IndividualCrudRepository individualCrudRepository;

    private final WalletMapper walletMapper;

    @Override
    public List<WalletDto> getWallet(String icp) {
        List<Wallet> wallets = getIndividual(icp).getWallet();
        if (!wallets.isEmpty()) {
            return wallets
                    .stream()
                    .filter(Wallet::isActual)
                    .map(wallet -> {
                        WalletDto walletDto = walletMapper.toDto(wallet);
                        walletDto.setIcp(icp);
                        return walletDto;
                    })
                    .toList();
        }
        throw new WalletNotFoundException("No wallets for icp "
                + icp
                + " found");
    }

    @Override
    public WalletDto saveWallet(WalletDto dto) {
        Individual ind = getIndividual(dto.getIcp());
        List<Wallet> wallets = ind.getWallet();
        List<Wallet> oldWallet = wallets.stream()
                .filter(wallet -> dto.getCurrencyType().equals(wallet.getCurrencyType()) && wallet.isActual())
                .toList();
        oldWallet.forEach(wal -> {
            dto.setBalance(dto.getBalance().add(wal.getBalance()));
            wal.setBalance(BigDecimal.ZERO);
        });
        markWalletAsNotActual(oldWallet);

        Wallet wallet = walletMapper.toEntity(dto);
        wallet.setUuid(UUID.randomUUID().toString());
        wallet.setActual(true);
        wallet.setIndividual(ind);

        log.info("For icp {} created new Wallet: {}", dto.getIcp(), wallet);

        walletCrudRepository.save(wallet);

        WalletDto walletDto = walletMapper.toDto(wallet);
        walletDto.setIcp(dto.getIcp());
        return walletDto;
    }
    private void markWalletAsNotActual(List<Wallet> list) {
        list.forEach(wallet -> {
            if (wallet.isActual()) {
                wallet.setActual(false);
            }
        });
    }

    private Individual getIndividual(String icp) {
        return individualCrudRepository
                .findByIcp(icp)
                .orElseThrow(()-> new IndividualNotFoundException(
                        "Individual with icp "
                        + icp
                        + " not found"));
    }
}
