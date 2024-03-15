package org.kata.service;

import org.kata.controller.dto.IndividualDto;
import org.springframework.stereotype.Service;

@Service
public interface IntrudersDetectionService {
     void checkIndividual(IndividualDto dto);

     boolean isInvalidPhoneNumber(IndividualDto dto);

     boolean isInvalidPassport(IndividualDto dto);
}
