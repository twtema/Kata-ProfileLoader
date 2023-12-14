package org.kata.service;

import org.kata.controller.dto.IndividualDto;
import org.kata.entity.Individual;

public interface IndividualService {
    IndividualDto getIndividual(String icp);
    IndividualDto getIndividualByPhone(String phone);

    IndividualDto saveIndividual(IndividualDto dto);

    Individual getIndividualEntity(String icp);
}
