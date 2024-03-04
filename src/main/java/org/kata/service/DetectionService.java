package org.kata.service;


import org.kata.controller.dto.IndividualDto;

public interface DetectionService {
    void checkIndividual(IndividualDto dto);

    void terroristDetection(IndividualDto dto);

    void intrudersDetection(IndividualDto dto);

    void debtorsDetection(IndividualDto dto);

    void fraudstersDetection(IndividualDto dto);
}