package org.kata.service;

import org.kata.controller.dto.IndividualDto;

public interface IndividualService {
    IndividualDto getIndividual(String icp);

    IndividualDto saveIndividual(IndividualDto dto);

    IndividualDto getIndividual(String icp, String uuid);
}
