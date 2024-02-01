package org.kata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.Individual;
import org.kata.entity.IndividualRelatedEntity;
import org.kata.exception.IndividualNotFoundException;
import org.kata.repository.IndividualCrudRepository;
import org.kata.service.IndividualService;
import org.kata.service.mapper.IndividualMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class IndividualServiceImp implements IndividualService {

    private final IndividualCrudRepository individualCrudRepository;
    private final IndividualMapper individualMapper;

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

        return individualMapper.toDto(entity);
    }


    @Override
    public void deleteIndividual(String icp) {
        Individual entity = individualCrudRepository.findByIcp(icp)
                .orElseThrow(() -> new IndividualNotFoundException("Individual with icp: " + icp + " not found"));
        log.info("Delete Individual with icp: {}", icp);
        individualCrudRepository.delete(entity);
    }


    @Override
    public IndividualDto getIndividual(String icp, String uuid) {
        if (uuid.equals("uuid")) {
            return getIndividual(icp);
        } else {
            throw new IllegalArgumentException("Invalid Type");
        }

    }

    @Override
    public long getProfileCompletionInPercentage(String icp) {
        Individual individual = individualCrudRepository.findByIcp(icp)
                .orElseThrow(() -> new IndividualNotFoundException("Individual with icp: " + icp + " not found"));
        List<Boolean> ifTrue = new ArrayList<>();
        ifTrue.add(!individual.getName().isEmpty());
        ifTrue.add(!individual.getSurname().isEmpty());
        ifTrue.add(!individual.getPatronymic().isEmpty());
        ifTrue.add(!individual.getFullName().isEmpty());
        ifTrue.add(!individual.getPlaceOfBirth().isEmpty());
        ifTrue.add(!individual.getCountryOfBirth().isEmpty());
        ifTrue.add(individual.getBirthDate() != null);
        ifTrue.add(!individual.getDocuments().isEmpty());
        ifTrue.add(!individual.getContacts().isEmpty());
        ifTrue.add(!individual.getAddress().isEmpty());
        ifTrue.add(!individual.getAvatar().isEmpty());
        ifTrue.add(!individual.getWallet().isEmpty());
        long trueCount =  ifTrue.stream().filter(element -> element.equals(true)).count();
        return 100 * trueCount / 12;
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