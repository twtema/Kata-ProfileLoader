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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(key = "#icp", value = "icpIndividual")
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
        log.warn("dto.getIcp is {}", dto.getIcp());


        if (entity.getUuid() == null) {
            entity.setUuid(generateUuid());
        }

        processCollection(entity.getAddress(), entity);
        processCollection(entity.getDocuments(), entity);
        processCollection(entity.getContacts(), entity);
        processCollection(entity.getAvatar(), entity);
        processCollection(entity.getWallet(), entity);

        entity.getAvatar().get(0).setActual(true);

        log.info("Create new Individual: {}", entity);

        individualCrudRepository.save(entity);



        return individualMapper.toDto(entity);
    }


    @Override
    @CacheEvict(key = "#icp", value = {"icpIndividual", "icpAddress", "icpAvatar", "icpContactMedium", "icpDocuments", "icpWallet"})
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