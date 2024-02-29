package org.kata.service;

import org.kata.controller.dto.IndividualDto;
import org.kata.entity.BlackListDocument;

public interface DebtCheckService {
    void saveBlackListDocument(int count);
    void checkBlackListDocumentsWithExisting(IndividualDto dto);
}
