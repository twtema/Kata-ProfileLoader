package org.kata.service;

import org.kata.controller.dto.IndividualDto;

public interface DebtDetectionService {
    void checkIndividual(IndividualDto dto);
}
