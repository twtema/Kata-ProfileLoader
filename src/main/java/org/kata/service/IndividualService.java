package org.kata.service;

import org.kata.controller.dto.IndividualDto;
import org.kata.entity.Individual;

public interface IndividualService {
    IndividualDto getIndividual(String icp);

    IndividualDto getIndividualByPhone(String phone);

    IndividualDto saveIndividual(IndividualDto dto);

    IndividualDto saveIndividualAndSendMessage(IndividualDto dto);

    Individual getIndividualEntity(String icp);

    IndividualDto getIndividual(String icp, String uuid);

    void deleteIndividual(String icp);

    IndividualDto getTestIndividual();

}
