package org.kata.service;

import org.kata.controller.dto.IndividualDto;

public interface DebtCheckService {
    void saveBlackListDocument(int count);
    void checkBlackListDocumentsWithExisting(IndividualDto dto);
}
