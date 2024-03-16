package org.kata.repository;

import org.kata.entity.BankCard;
import org.springframework.data.repository.CrudRepository;

public interface BankCardCrudRepository extends CrudRepository<BankCard, String> {
}
