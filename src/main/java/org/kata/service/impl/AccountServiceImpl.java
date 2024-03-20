package org.kata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kata.controller.dto.AccountDto;
import org.kata.controller.dto.AccountOperationDto;
import org.kata.entity.Account;
import org.kata.entity.Individual;
import org.kata.entity.enums.ContactMediumType;
import org.kata.entity.enums.CurrencyType;
import org.kata.exception.*;
import org.kata.repository.AccountCrudRepository;
import org.kata.repository.IndividualCrudRepository;
import org.kata.service.AccountService;
import org.kata.service.ContactMediumService;
import org.kata.service.mapper.AccountMapper;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountCrudRepository accountCrudRepository;

    private final IndividualCrudRepository individualCrudRepository;

    private final AccountMapper accountMapper;

    private final ContactMediumService contactMediumService;

    private final CacheManager cacheManager;

    private static final String ACCOUNT_OPERATION = "accountOperation";
    private static final String LOG_AOD_FROM_CACHE_RECEIVED = "AccountOperationDto from cache received. Dto: {}";
    private static final String LOG_CAO_FROM_CACHE_REMOVED = "CacheAccountOperation from cache removed. Cao: {}";
    private static final String LOG_ACCOUNT_DEPOSIT_SUCCESSFUL = "Account deposit successful. Deposited entity: {}";
    private static final String LOG_ACCOUNT_WITHDRAW_SUCCESSFUL = "Account withdraw successful. Withdraw entity: {}";
    private static final String LOG_ACCOUNT_SUCCESSFULLY_LOCKED = "Account successfully locked. Locked entity: {}";
    private static final String LOG_ACCOUNT_SUCCESSFULLY_UNLOCKED = "Account successfully unlocked. Unlocked entity: {}";
    private static final String EX_ACCOUNT_NOT_FOUND_IN_DB = "Account with ID %s not found in database";

    @Override
    public List<AccountDto> getAccount(String icp) {
        List<Account> accounts = getIndividual(icp).getAccount();
        if (!accounts.isEmpty()) {
            return accounts
                    .stream()
                    .filter(Account::isActual)
                    .map(account -> {
                        AccountDto accountDto = accountMapper.toDto(account);
                        accountDto.setIcp(icp);
                        return accountDto;
                    })
                    .toList();
        }
        throw new AccountNotFoundException(String.format("No accounts for icp %s found", icp));
    }

    @Override
    public AccountDto saveAccount(AccountDto dto) {
        Individual ind = getIndividual(dto.getIcp());
        List<Account> accounts = ind.getAccount();
        List<Account> oldAccount = accounts.stream()
                .filter(account -> dto.getCurrencyType().equals(account.getCurrencyType()) && account.isActual())
                .toList();
        oldAccount.forEach(wal -> {
            dto.setBalance(dto.getBalance().add(wal.getBalance()));
            wal.setBalance(BigDecimal.ZERO);
        });
        markAccountAsNotActual(oldAccount);

        Account account = accountMapper.toEntity(dto);
        account.setUuid(UUID.randomUUID().toString());
        account.setActual(true);
        account.setIndividual(ind);

        log.info("For icp {} created new Account: {}", dto.getIcp(), account);

        try {
            accountCrudRepository.save(account);
            log.debug("Saved Account to the database: {}", account);
        } catch (Exception e) {
            log.warn("Failed to save Account to the database.", e);
        }

        AccountDto accountDto = accountMapper.toDto(account);
        accountDto.setIcp(dto.getIcp());
        return accountDto;
    }

    private void markAccountAsNotActual(List<Account> list) {
        list.forEach(account -> {
            if (account.isActual()) {
                account.setActual(false);
            }
        });
    }

    private Individual getIndividual(String icp) {
        return individualCrudRepository
                .findByIcp(icp)
                .orElseThrow(() -> new IndividualNotFoundException(String.format("Individual with icp %s not found", icp)));
    }

    private Account getAccountByAccountId(String accountId) {
        return accountCrudRepository
                .findByAccountId(accountId)
                .filter(Account::isActual)
                .orElseThrow(() -> new AccountNotFoundException(String.format(EX_ACCOUNT_NOT_FOUND_IN_DB, accountId)));
    }

    public AccountDto getAccountByMobileAndCurrency(String mobile, CurrencyType currencyType) {

        return accountMapper.toDto(contactMediumService
                .getContactMediumByTypeAndValue(ContactMediumType.PHONE, mobile)
                .getIndividual()
                .getAccount()
                .stream()
                .filter(account -> account.getCurrencyType().compareTo(currencyType) == 0 && account.isActual())
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException(
                        String.format("Account for mobile %s with currency %s not found", mobile, currencyType)
                )));
    }

    @Override
    public AccountDto update(String accountId, BigDecimal balance) {
        Account account = accountCrudRepository.findByAccountId(accountId).orElseThrow(
                () -> new AccountNotFoundException(String.format(EX_ACCOUNT_NOT_FOUND_IN_DB, accountId))
        );
        account.setBalance(balance);
        return accountMapper.toDto(accountCrudRepository.save(account));
    }


    /**
     * Executes an account operation based on data retrieved from the cache.
     * The operation can be one of the following types: deposit, withdrawal,
     * transfer, locking, or unlocking of an account.
     *
     * @param uuid The unique identifier of the operation, used to extract
     *             operation data from the cache.
     * @return {@link AccountDto} The DTO of the account on which the operation was performed.
     * @throws AccountOperationException if the operation cannot be executed.
     *                                   This can be caused by various reasons,
     *                                   including the absence of operation data in the cache,
     *                                   insufficient balance for withdrawal or transfer,
     *                                   attempting to perform an operation on an inactive account, etc.
     * @throws AccountInCacheNotFoundException if the operation data is not found in the cache.
     *
     * @Этот метод и все методы вызываемые дополнительно проверить после успешного запуска redis.
     */
    @Override
    public AccountDto performAccountOperation(String uuid) throws AccountOperationException {

        AccountOperationDto aod;
        Cache cacheAccountOperation = cacheManager.getCache(ACCOUNT_OPERATION);
        Cache.ValueWrapper valueWrapper = cacheAccountOperation.get(uuid);

        if (valueWrapper != null) {
            aod = (AccountOperationDto) valueWrapper.get();
            log.debug(LOG_AOD_FROM_CACHE_RECEIVED, aod);
        } else {
            throw new AccountInCacheNotFoundException(uuid);
        }

        AccountDto accountDto = switch (aod.getOperationType()) {
            case DEPOSIT -> deposit(aod);
            case WITHDRAW -> withdraw(aod);
            case TRANSFER -> transfer(aod);
            case LOCKING -> locking(aod);
            case UNLOCKING -> unlocking(aod);
        };

        cacheAccountOperation.evict(uuid);
        log.debug(LOG_CAO_FROM_CACHE_REMOVED, cacheAccountOperation);

        return accountDto;
    }

    /**
     * Performs a deposit operation on an account. This method retrieves the account
     * based on the account ID provided in the AccountOperationDto, verifies that the account
     * is active and the currency type matches the operation. It then updates the account's
     * balance by adding the amount specified in the AccountOperationDto and saves the changes
     * to the repository.
     *
     * @param aod The AccountOperationDto containing details of the deposit operation,
     *            including the account ID, the amount to be deposited, and the currency type.
     * @return {@link AccountDto} The DTO of the account after the deposit operation has been performed.
     * @throws AccountOperationException if the account cannot be found, is not active, or
     *                                   if the currency type does not match the account's currency type.
     */
    private AccountDto deposit(AccountOperationDto aod) throws AccountOperationException {
        Account entity = getAccount(aod);
        verifyAccountIsActive(entity);
        verifyCurrencyOperationType(entity, aod.getCurrencyType());
        entity.setBalance(entity.getBalance().add(aod.getAmount()));
        accountCrudRepository.save(entity);
        log.debug(LOG_ACCOUNT_DEPOSIT_SUCCESSFUL, entity);
        return accountMapper.toDto(entity);
    }

    /**
     * Performs a withdrawal operation on an account. This method retrieves the account
     * based on the account ID provided in the AccountOperationDto, verifies that the account
     * is active, and checks if the currency type matches the operation. It then updates the account's
     * balance by subtracting the amount specified in the AccountOperationDto. If the account does not have
     * sufficient funds, an InsufficientFundsException is thrown. Otherwise, the changes are saved
     * to the repository.
     *
     * @param aod The AccountOperationDto containing details of the withdrawal operation,
     *            including the account ID, the amount to be withdrawn, and the currency type.
     * @return {@link AccountDto} The DTO of the account after the withdrawal operation has been performed.
     * @throws AccountOperationException if the account cannot be found, is not active, or
     *                                   if the currency type does not match the account's currency type.
     * @throws InsufficientFundsException if the account does not have enough balance to cover the withdrawal.
     */
    private AccountDto withdraw(AccountOperationDto aod) throws AccountOperationException {

        Account entity = getAccount(aod);
        verifyAccountIsActive(entity);
        verifyCurrencyOperationType(entity, aod.getCurrencyType());

        if (entity.getBalance().compareTo(aod.getAmount()) >= 0) {
            entity.setBalance(entity.getBalance().subtract(aod.getAmount()));
            accountCrudRepository.save(entity);
        } else {
            throw new InsufficientFundsException(entity.getBalance(), aod.getAmount());
        }
        log.debug(LOG_ACCOUNT_WITHDRAW_SUCCESSFUL, entity);
        return accountMapper.toDto(entity);
    }

    /**
     * Performs a transfer operation between two accounts. This method retrieves both the source (withdrawal)
     * and destination accounts based on the account ID provided in the AccountOperationDto. It verifies that both
     * accounts are active and that their currency types match the operation. The method then checks if the source
     * account has sufficient funds for the transfer. If so, it updates the balances of both accounts accordingly
     * and saves the changes to the repository. If the source account does not have sufficient funds, an
     * InsufficientFundsException is thrown.
     *
     * @param aod The AccountOperationDto containing details of the transfer operation, including the account ID
     *            of the source account, the amount to be transferred, and the currency type. Note: The method
     *            currently does not differentiate between the source and destination accounts based on the
     *            AccountOperationDto provided. This might need to be adjusted to correctly identify both accounts.
     * @return {@link AccountDto} The DTO of the source (withdrawal) account after the transfer operation has been performed.
     * @throws AccountOperationException if either account cannot be found, is not active, or if the currency type
     *                                   does not match the account's currency type.
     * @throws InsufficientFundsException if the source account does not have enough balance to cover the transfer.
     */
    private AccountDto transfer(AccountOperationDto aod) throws AccountOperationException {

        Account withdrawEntity = getAccount(aod);
        Account destinationEntity = getAccount(aod);

        verifyAccountIsActive(withdrawEntity, destinationEntity);
        verifyCurrencyOperationType(withdrawEntity, aod.getCurrencyType());
        verifyCurrencyOperationType(destinationEntity, aod.getCurrencyType());

        if (withdrawEntity.getBalance().compareTo(aod.getAmount()) >= 0) {
            withdrawEntity.setBalance(withdrawEntity.getBalance().subtract(aod.getAmount()));
            destinationEntity.setBalance(destinationEntity.getBalance().add(aod.getAmount()));
            accountCrudRepository.save(withdrawEntity);
            accountCrudRepository.save(destinationEntity);
        } else {
            throw new InsufficientFundsException(withdrawEntity.getBalance(), aod.getAmount());
        }
        log.debug(LOG_ACCOUNT_WITHDRAW_SUCCESSFUL, withdrawEntity);
        return accountMapper.toDto(withdrawEntity);
    }

    /**
     * Locks an account, making it inactive. This method retrieves the account based on the account ID provided
     * in the AccountOperationDto. It then sets the account's 'actual' status to false, effectively locking the account,
     * and saves the changes to the repository.
     *
     * @param aod The AccountOperationDto containing details of the account to be locked, including the account ID.
     * @return {@link AccountDto} The DTO of the account after it has been locked.
     */
    private AccountDto locking(AccountOperationDto aod) {
        Account entity = getAccount(aod);
        entity.setActual(false);
        accountCrudRepository.save(entity);
        log.debug(LOG_ACCOUNT_SUCCESSFULLY_LOCKED, entity);
        return accountMapper.toDto(entity);
    }

    /**
     * Unlocks an account, making it active. This method retrieves the account based on the account ID
     * provided in the AccountOperationDto. It then sets the account's 'actual' status to true,
     * effectively unlocking the account, and saves the changes to the repository.
     *
     * @param aod The AccountOperationDto containing details of the account to be unlocked, including the account ID.
     * @return {@link AccountDto} The DTO of the account after it has been unlocked.
     */
    private AccountDto unlocking(AccountOperationDto aod) {
        Account entity = getAccount(aod);
        entity.setActual(true);
        accountCrudRepository.save(entity);
        log.debug(LOG_ACCOUNT_SUCCESSFULLY_UNLOCKED, entity);
        return accountMapper.toDto(entity);
    }

    /**
     * Verifies that the account(s) are active. This method iterates through an array of accounts and checks
     * if each one is active (the 'actual' field is true). If any of the accounts are found to be inactive,
     * it throws an AccountNotActiveException.
     *
     * @param accounts An array of accounts to be checked for activity.
     * @throws AccountNotActiveException if any of the accounts are inactive.
     */
    private void verifyAccountIsActive(Account... accounts) throws AccountNotActiveException {
        for (Account account : accounts) {
            if (!account.isActual()) throw new AccountNotActiveException(account.getAccountId());
        }
    }

    /**
     * Verifies that the currency type of the operation matches the currency type of the account. This method checks if the
     * currency type specified in the operation (represented by the {@code CurrencyType} parameter) matches the currency type
     * of the provided account. If the currency types do not match, it throws an {@link AccountWrongCurrencyTypeException}.
     *
     * @param account The account whose currency type is to be verified against the operation's currency type.
     * @param ct The currency type specified in the operation.
     * @throws AccountWrongCurrencyTypeException if the account's currency type does not match the operation's currency type.
     */
    private void verifyCurrencyOperationType(Account account, CurrencyType ct)
            throws AccountWrongCurrencyTypeException {
        if (account.getCurrencyType() != ct) throw new AccountWrongCurrencyTypeException(account.getAccountId(), ct);
    }

    /**
     * Retrieves an account based on the account ID provided in the AccountOperationDto. If the account cannot be found,
     * it throws an AccountNotFoundException. This method ensures that operations are only performed on existing accounts,
     * enhancing the reliability of the account management process.
     *
     * @param aod The AccountOperationDto containing the account ID of the account to be retrieved.
     * @return The Account entity corresponding to the account ID provided in the AccountOperationDto.
     * @throws AccountNotFoundException if the account with the specified ID does not exist in the database.
     */
    private Account getAccount(AccountOperationDto aod) throws AccountNotFoundException {
        return accountCrudRepository.findByAccountId(aod.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException(
                        String.format(EX_ACCOUNT_NOT_FOUND_IN_DB, aod.getAccountId())));
    }
}