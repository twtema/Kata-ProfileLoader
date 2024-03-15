package org.kata.service.mapper;

import org.kata.controller.dto.BankCardDto;
import org.kata.entity.BankCard;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BankCardMapper {
    BankCard toEntity(BankCardDto bankCardDto);

    BankCardDto toDto(BankCard bankCard);

    List<BankCardDto> toDto(List<BankCard> bankCard);
}