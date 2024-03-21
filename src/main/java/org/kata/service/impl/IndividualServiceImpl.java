package org.kata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.IndividualDto;
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

import static org.kata.utils.Constants.*;
import static org.kata.utils.Constants.ConstantsServiceLogs.*;
import static org.kata.utils.Constants.ConstantsServicesErrors.*;


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
                .orElseThrow(() -> new IndividualNotFoundException(String.format(ERR_INDIVIDUAL_WITH_ICP_NOT_FOUND, icp)));
        if (!(entity == null)) {
            return individualMapper.toDto(entity);
        } else {
            throw new IndividualNotFoundException(String.format(ERR_INDIVIDUAL_UUID_NOT_FOUND, icp));
        }
    }

    @Override
    public IndividualDto getIndividualByPhone(String phone) {
        Individual entity = individualCrudRepository.findByPhone(phone)
                .orElseThrow(() -> new IndividualNotFoundException(
                        String.format(ERR_INDIVIDUAL_WITH_PHONE_NOT_FOUND, phone)
                ));
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

        processCollection(entity.getAddress(), entity);
        processCollection(entity.getDocuments(), entity);
        processCollection(entity.getContacts(), entity);
        processCollection(entity.getAvatar(), entity);
        processCollection(entity.getWallet(), entity);

        entity.getAvatar().get(0).setActual(true);

        log.info(LOG_CREATE_NEW_INDIVIDUAL, entity);

        individualCrudRepository.save(entity);

        try {
            individualCrudRepository.save(entity);
            log.debug(LOG_SAVED_INDIVIDUAL_TO_THE_DATABASE, entity);
        } catch (Exception e) {
            log.warn(LOG_FAILED_TO_SAVE_INDIVIDUAL_TO_THE_DATABASE, e);
        }

        return individualMapper.toDto(entity);
    }


    @Override
    public void deleteIndividual(String icp) {
        Individual entity = individualCrudRepository.findByIcp(icp)
                .orElseThrow(() -> new IndividualNotFoundException(String.format(ERR_INDIVIDUAL_WITH_ICP_NOT_FOUND, icp)));


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
        if (uuid.equals(UUID_STRING_VALUE)) {
            return getIndividual(icp);
        } else {
            throw new IllegalArgumentException(ERR_INVALID_TYPE);
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
                .orElseThrow(() -> new IndividualNotFoundException(String.format(ERR_INDIVIDUAL_WITH_ICP_NOT_FOUND, icp)));
    }
}