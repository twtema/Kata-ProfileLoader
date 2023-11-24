package org.kata.repository;

import org.kata.entity.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressCrudRepository extends CrudRepository<Address, String> {
}
