package org.kata.repository;

import org.kata.entity.BankCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankCardCrudRepository extends CrudRepository<BankCard, String> {
}
