package org.kata.repository;


import org.apache.el.stream.Stream;
import org.kata.entity.Wallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletCrudRepository extends CrudRepository<Wallet, String> {

    Optional<Wallet> findByWalletId(String walletId);
}
