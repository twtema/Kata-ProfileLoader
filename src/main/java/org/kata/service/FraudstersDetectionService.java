package org.kata.service;

import org.kata.controller.dto.ContactMediumDto;
import org.kata.controller.dto.IndividualDto;
import org.springframework.stereotype.Service;

@Service
public interface FraudstersDetectionService {
     void checkContact(ContactMediumDto dto);

     void checkIndividual(IndividualDto dto);
}
