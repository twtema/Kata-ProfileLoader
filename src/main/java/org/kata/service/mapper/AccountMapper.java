package org.kata.service.mapper;

import org.kata.controller.dto.AccountDto;
import org.kata.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {
    Account toEntity(AccountDto accountDto);

    AccountDto toDto(Account account);

    List<AccountDto> toDto(List<Account> account);
}