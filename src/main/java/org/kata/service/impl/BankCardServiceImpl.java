package org.kata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.BankCardDto;
import org.kata.entity.Account;
import org.kata.entity.BankCard;
import org.kata.entity.Individual;
import org.kata.exception.BankCardNotFoundException;
import org.kata.exception.IndividualNotFoundException;
import org.kata.repository.BankCardCrudRepository;
import org.kata.repository.IndividualCrudRepository;
import org.kata.service.BankCardService;
import org.kata.service.IndividualService;
import org.kata.service.mapper.BankCardMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BankCardServiceImpl implements BankCardService {

    private final BankCardCrudRepository bankCardCrudRepository;

    private final IndividualCrudRepository individualCrudRepository;

    private final IndividualService individualService;

    private final BankCardMapper bankCardMapper;


    @Override
    public List<BankCardDto> getAllBankCards(String icp) {
        Optional<Individual> individual = individualCrudRepository.findByIcp(icp);

        if (individual.isPresent()) {
            List<BankCard> bankCards = individual.get().getBankCard();

            if (!bankCards.isEmpty()) {
                List<BankCardDto> bankCardDtos = bankCardMapper.toDto(bankCards);
                bankCardDtos.forEach(card -> card.setIcp(icp));
                bankCardDtos.forEach(card -> card.setActual(card.isActual()));

                return bankCardDtos;
            } else {
                throw new BankCardNotFoundException("No BankCard found for individual with icp: " + icp);
            }
        } else {
            throw new IndividualNotFoundException("Individual with icp: " + icp + " not found");
        }
    }

    @Override
    public BankCardDto saveBankCard(BankCardDto dto) {
        Optional<Individual> individual = individualCrudRepository.findByIcp(dto.getIcp());

        return individual.map(ind -> {
            List<BankCard> bankCards = ind.getBankCard();
            if (bankCards == null) {
                bankCards = new ArrayList<>();
                ind.setBankCard(bankCards);
            }
            if (bankCardExists(ind, dto.getCardNumber(), dto.getCvv())) {
                throw new BankCardNotFoundException("Bank card with number: " + dto.getCardNumber() + " already exists.");
            }
            List<BankCard> markOldCard = bankCards.stream()
                    .filter(card -> dto.getBankCardType().equals(card.getBankCardType()))
                    .collect(Collectors.toList());
            markBankCardAsNotActual(markOldCard);

            BankCard bankCard = bankCardMapper.toEntity(dto);
            bankCard.setUuid(UUID.randomUUID().toString());
            bankCard.setActual(true);
            bankCard.setIndividual(ind);

            List<Account> accounts = ind.getAccount();
            if (accounts != null) {
                for (Account account : accounts) {
                    bankCard.setAccount(account);
                }
            }

            log.info("For icp {} created new BankCard: {}", dto.getIcp(), bankCard);

            try {
                bankCardCrudRepository.save(bankCard);
                log.debug("Saved bankCard to the database: {}", bankCard);
            } catch (Exception e) {
                log.warn("Failed to save bankCard to the database.", e);
            }

            BankCardDto bankCardDto = bankCardMapper.toDto(bankCard);
            bankCardDto.setIcp(dto.getIcp());
            return bankCardDto;
        }).orElseThrow(() -> new IndividualNotFoundException("Individual with icp: " + dto.getIcp() + " not found"));
    }

    @Override
    public List<BankCardDto> getAllBankCards(String icp, String uuid) {
        if (uuid.equals("uuid")) {
            return getAllBankCards(icp);
        } else {
            throw new IllegalArgumentException("Invalid type");
        }
    }

    @Override
    public void deleteBankCard(String icp, List<String> cardNumbers) {
        List<BankCard> bankCards = getIndividual(icp).getBankCard();
        for (String cardNumber : cardNumbers) {
            List<BankCard> cardToDelete = bankCards.stream()
                    .filter(card -> card.getCardNumber().equals(cardNumber))
                    .toList();
            bankCards.removeAll(cardToDelete);
            if (!cardToDelete.isEmpty()) {
                bankCardCrudRepository.deleteAll(cardToDelete);
            } else {
                throw new BankCardNotFoundException("Bank card with number " + cardNumber + " not found for individual with icp: " + icp);
            }
        }
    }

    public boolean bankCardExists(Individual individual, String cardNumber, Integer cvv) {
        if (individual.getBankCard() == null) {
            return false;
        } else if (cardNumber.length() != 16) {
            throw new IllegalArgumentException("Card number must contain 16 digits.");
        } else if (String.valueOf(cvv).length() != 3) {
            throw new IllegalArgumentException("CVV code must contain 3 digits.");
        }

        return individual.getBankCard().stream().anyMatch(card ->
                cardNumber.equals(card.getCardNumber()) && cvv.equals(card.getCvv()));
    }

    private Individual getIndividual(String icp) {
        return individualService.getIndividualEntity(icp);
    }

    private void markBankCardAsNotActual(List<BankCard> list) {
        list.forEach(bankCard -> {
            if (bankCard.isActual()) {
                bankCard.setActual(false);
            }
        });
    }
}
