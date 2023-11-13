package org.kata.service;

import org.kata.controller.dto.IndividualDto;

public interface IndividualService {
    IndividualDto getIndividual(String icp);
    IndividualDto getIndividualByPhone(String phone);

    IndividualDto saveIndividual(IndividualDto dto);
}
