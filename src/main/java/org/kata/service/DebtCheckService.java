package org.kata.service;

import org.kata.controller.dto.IndividualDto;

public interface DebtCheckService {
    void checkBlackListDocumentsWithExisting(IndividualDto dto);
}
