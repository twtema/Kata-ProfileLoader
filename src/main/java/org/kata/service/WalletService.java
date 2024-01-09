package org.kata.service;
import org.kata.controller.dto.WalletDto;

import java.util.List;

public interface WalletService {

    List<WalletDto> getWallet(String icp);

    WalletDto saveWallet(WalletDto dto);

}
