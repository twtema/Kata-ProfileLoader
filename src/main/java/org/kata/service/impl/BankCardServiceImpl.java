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
import org.kata.service.mapper.BankCardMapper;
import org.springframework.stereotype.Service;

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
            if (bankCardExists(ind, dto.getCardNumber())) {
                throw new BankCardNotFoundException("Bank card with number: " + dto.getCardNumber() + " already exists.");
            }
            List<BankCard> bankCards = ind.getBankCard();
            List<BankCard> markOldCard = bankCards.stream()
                    .filter(card -> dto.getBankCardType().equals(card.getBankCardType()))
                    .collect(Collectors.toList());
            markBankCardAsNotActual(markOldCard);

            BankCard bankCard = bankCardMapper.toEntity(dto);
            bankCard.setUuid(UUID.randomUUID().toString());
            bankCard.setActual(true);
            bankCard.setIndividual(ind);

            for (Account account : ind.getAccount()) {
                bankCard.setAccount(account);
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

    private void markBankCardAsNotActual(List<BankCard> list) {
        list.forEach(bankCard -> {
            if (bankCard.isActual()) {
                bankCard.setActual(false);
            }
        });
    }

    @Override
    public List<BankCardDto> getAllBankCards(String icp, String uuid) {
        if (uuid.equals("uuid")) {
            return getAllBankCards(icp);
        } else {
            throw new IllegalArgumentException("Invalid type");
        }
    }

    private boolean bankCardExists(Individual individual, String cardNumber) {
        return individual.getBankCard().stream()
                .anyMatch(card -> cardNumber.equals(card.getCardNumber()));
    }
}
