package org.kata.service.impl;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.Individual;
import org.kata.entity.IndividualRelatedEntity;
import org.kata.exception.IndividualNotFoundException;
import org.kata.repository.IndividualCrudRepository;
import org.kata.service.IndividualService;
import org.kata.service.mapper.IndividualMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Collection;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class IndividualServiceImp implements IndividualService {

    private final IndividualCrudRepository individualCrudRepository;
    private final IndividualMapper individualMapper;
    private final Jedis jedis;
    private final ObjectMapper objectMapper;


    @Override
    public IndividualDto getIndividual(String icp) {
        Individual entity = individualCrudRepository.findByIcp(icp)
                .orElseThrow(() -> new IndividualNotFoundException("Individual with icp: " + icp + " not found"));
        return individualMapper.toDto(entity);
    }

    @Override
    public IndividualDto getIndividualByPhone(String phone) {
        Individual entity = individualCrudRepository.findByPhone(phone)
                .orElseThrow(() -> new IndividualNotFoundException("Individual with phone: " + phone + " not found"));
        return individualMapper.toDto(entity);
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

        log.info("Create new Individual: {}", entity);

        individualCrudRepository.save(entity);

        saveDataToRedis(entity);

        return individualMapper.toDto(entity);
    }

    private void saveDataToRedis(Individual entity) {
        try {
            String individualJson = objectMapper.writeValueAsString(entity);
            jedis.setex("individual:" + entity.getUuid(), 7200, individualJson);
        } catch (JsonProcessingException e) {
            log.error("Error saving data to Reddis: {}", e.getMessage());
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