package org.kata.repository;


import org.kata.entity.Wallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletCrudRepository extends CrudRepository<Wallet, String> {
}
