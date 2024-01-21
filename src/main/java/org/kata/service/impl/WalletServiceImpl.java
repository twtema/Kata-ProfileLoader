package org.kata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.WalletDto;
import org.kata.entity.Individual;
import org.kata.entity.Wallet;
import org.kata.entity.enums.ContactMediumType;
import org.kata.entity.enums.CurrencyType;
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
import org.kata.service.ContactMediumService;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletCrudRepository walletCrudRepository;

    private final IndividualCrudRepository individualCrudRepository;

    private final WalletMapper walletMapper;

    private final ContactMediumService contactMediumService;



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
                .orElseThrow(() -> new IndividualNotFoundException(
                        "Individual with icp " + icp + " not found"));
    }

    private Wallet getWalletByWalletId(String walletId) {
        return walletCrudRepository
                .findByWalletId(walletId)
                .filter(Wallet::isActual)
                .orElseThrow(() -> new WalletNotFoundException(
                        "Wallet with id "
                                + walletId
                                + " not found"
                ));
    }

    public WalletDto getWalletByMobileAndCurrency(String mobile, CurrencyType currencyType) {

        return walletMapper.toDto(contactMediumService
                .getContactMediumByTypeAndValue(ContactMediumType.PHONE, mobile)
                .getIndividual()
                .getWallet()
                .stream()
                .filter(wallet -> wallet.getCurrencyType().compareTo(currencyType) == 0
                        && wallet.isActual())
                .findFirst()
                .orElseThrow(() -> new WalletNotFoundException(
                        "Wallet for mobile "
                                + mobile
                                + " with currency "
                                + currencyType
                                + " not found"

                )));
    }

    @Override
    public WalletDto update(String walletId, BigDecimal balance) {
        Wallet wallet = walletCrudRepository.findByWalletId(walletId).orElseThrow(
                () -> new WalletNotFoundException("Wallet with walletId "
                        + walletId
                        + " not found")
        );
        wallet.setBalance(balance);
        return walletMapper.toDto(walletCrudRepository.save(wallet));
    }
}
