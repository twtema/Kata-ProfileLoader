package org.kata.repository;


import org.kata.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountCrudRepository extends CrudRepository<Account, String> {

    Optional<Account> findByAccountId(String accountId);
}
