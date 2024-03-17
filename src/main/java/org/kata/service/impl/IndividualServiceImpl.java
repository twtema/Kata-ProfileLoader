package org.kata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.Account;
import org.kata.entity.BankCard;
import org.kata.entity.Individual;
import org.kata.entity.IndividualRelatedEntity;
import org.kata.exception.IndividualNotFoundException;
import org.kata.initTestIndiv.TestIndividualBuilder;
import org.kata.repository.IndividualCrudRepository;
import org.kata.service.IndividualService;
import org.kata.service.KafkaMessageSender;
import org.kata.service.mapper.IndividualMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class IndividualServiceImpl implements IndividualService {

    private final IndividualCrudRepository individualCrudRepository;
    private final IndividualMapper individualMapper;
    private final KafkaMessageSender kafkaMessageSender;
    private final TestIndividualBuilder testIndividualBuilder;


    @Override
    public IndividualDto getIndividual(String icp) {
        Individual entity = individualCrudRepository.findByIcp(icp)
                .orElseThrow(() -> new IndividualNotFoundException("Individual with icp: " + icp + " not found"));
        if (!(entity == null)) {
            return individualMapper.toDto(entity);
        } else {
            throw new IndividualNotFoundException("Individual with uuid: " + icp + " not found");
        }
    }

    @Override
    public IndividualDto getIndividualByPhone(String phone) {
        Individual entity = individualCrudRepository.findByPhone(phone)
                .orElseThrow(() -> new IndividualNotFoundException("Individual with phone: " + phone + " not found"));
        return individualMapper.toDto(entity);
    }

    @Override
    public IndividualDto buildTestIndividual() {
        return individualMapper.toDto(testIndividualBuilder.individualInitializer());
    }

    @Override
    public IndividualDto saveIndividual(IndividualDto dto) {
        Individual entity = individualMapper.toEntity(dto);

        if (entity.getUuid() == null) {
            entity.setUuid(generateUuid());
        }

        /** Установка связей между сущностями */
        for (Account account : entity.getAccount()) {
            account.setIndividual(entity);
            for (BankCard bankCard : entity.getBankCard()) {
                bankCard.setIndividual(entity);
                bankCard.setAccount(account);
            }
        }

        processCollection(entity.getAddress(), entity);
        processCollection(entity.getDocuments(), entity);
        processCollection(entity.getContacts(), entity);
        processCollection(entity.getAvatar(), entity);
        processCollection(entity.getAccount(), entity);
        processCollection(entity.getBankCard(), entity);

        entity.getAvatar().get(0).setActual(true);

        log.info("Create new Individual: {}", entity);

        try {
            individualCrudRepository.save(entity);
            log.debug("Saved individual to the database: {}", entity);
        } catch (Exception e) {
            log.warn("Failed to save individual to the database.", e);
        }

        return individualMapper.toDto(entity);
    }


    @Override
    public void deleteIndividual(String icp) {
        Individual entity = individualCrudRepository.findByIcp(icp)
                .orElseThrow(() -> new IndividualNotFoundException("Individual with icp: " + icp + " not found"));


        individualCrudRepository.delete(entity);
    }



    @Override
    public IndividualDto saveIndividualAndSendMessage(IndividualDto dto) {
        saveIndividual(dto);
        kafkaMessageSender.sendMessage(dto);
        return dto;
    }

    @Override
    public IndividualDto getIndividual(String icp, String uuid) {
        if (uuid.equals("uuid")) {
            return getIndividual(icp);
        } else {
            throw new IllegalArgumentException("Invalid Type");
        }

    }


    private void processCollection(Collection<? extends IndividualRelatedEntity> collection, Individual entity) {
        if (!CollectionUtils.isEmpty(collection)) {
            collection.forEach(item -> {
                item.setUuid(generateUuid());
                item.setIndividual(entity);
            });
        }
    }

    private String generateUuid() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Individual getIndividualEntity(String icp) {
        return individualCrudRepository
                .findByIcp(icp)
                .orElseThrow(() -> new IndividualNotFoundException(
                        "Individual with icp " + icp + " not found"));
    }
}