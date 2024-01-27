package org.kata.service.mapper;

import org.kata.controller.dto.WalletDto;
import org.kata.entity.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WalletMapper {
    Wallet toEntity(WalletDto walletDto);

    WalletDto toDto(Wallet wallet);

    List<WalletDto> toDto(List<Wallet> wallet);
}
