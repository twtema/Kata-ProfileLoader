package org.kata.repository;

import org.kata.entity.Address;
import org.kata.entity.Individual;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressCrudRepository extends CrudRepository<Address, String> {

}
