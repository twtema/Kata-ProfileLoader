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
import org.kata.service.ContactMediumService;
import org.kata.service.WalletService;
import org.kata.service.mapper.WalletMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        Optional<Individual> individual = individualCrudRepository.findByIcp(icp);

        if (individual.isPresent()) {
            List<Wallet> wallets = individual.get().getWallet();

            if (!wallets.isEmpty()) {
                List<Wallet> actualWallets = wallets.stream()
                        .filter(Wallet::isActual)
                        .toList();
                List<WalletDto> walletDtos = walletMapper.toDto(actualWallets);
                walletDtos.forEach(w -> w.setIcp(icp));
                return walletDtos;
            } else {
                throw new WalletNotFoundException("No wallets for icp " + icp + " found");
            }
        } else {
            throw new IndividualNotFoundException("Individual with icp: " + icp + " not found");
        }
    }

    @Override
    public WalletDto saveWallet(WalletDto dto) {
        Optional<Individual> individual = individualCrudRepository.findByIcp(dto.getIcp());

        return individual.map(ind -> {
            List<Wallet> wallets = ind.getWallet();
            List<Wallet> markOldWallet = wallets.stream()
                    .filter(Wallet::isActual)
                    .filter(wallet -> dto.getCurrencyType().equals(wallet.getCurrencyType()))
                    .toList();
            markOldWallet.forEach(old -> {
                dto.setBalance(dto.getBalance().add(old.getBalance()));
                old.setBalance(BigDecimal.ZERO);
            });
            markWalletAsNotActual(markOldWallet);

            Wallet wallet = walletMapper.toEntity(dto);
            wallet.setUuid(UUID.randomUUID().toString());
            wallet.setActual(true);
            wallet.setIndividual(ind);

            log.info("For icp {} created new Wallet: {}", dto.getIcp(), wallet);

            walletCrudRepository.save(wallet);

            WalletDto walletDto = walletMapper.toDto(wallet);
            walletDto.setIcp(dto.getIcp());
            return walletDto;
        }).orElseThrow(() -> new IndividualNotFoundException("Individual with icp: " + dto.getIcp() + " not found"));
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
                        "Wallet with id " + walletId + " not found"
                ));
    }

    public WalletDto getWalletByPhoneAndCurrency(String phone, CurrencyType currencyType) {
        return walletMapper.toDto(contactMediumService
                .getContactMediumByTypeAndValue(ContactMediumType.PHONE, phone)
                .getIndividual()
                .getWallet()
                .stream()
                .filter(Wallet::isActual)
                .filter(wallet -> wallet.getCurrencyType().compareTo(currencyType) == 0)
                .findFirst()
                .orElseThrow(() -> new WalletNotFoundException(
                        "Wallet for phone " + phone + " with currency " + currencyType + " not found"
                )));
    }

    @Override
    public WalletDto updateWallet(String walletId, BigDecimal balance) {
        Wallet wallet = walletCrudRepository.findByWalletId(walletId).orElseThrow(() -> new WalletNotFoundException(
                "Wallet with walletId " + walletId + " not found")
        );
        wallet.setBalance(balance);
        return walletMapper.toDto(walletCrudRepository.save(wallet));
    }
}
