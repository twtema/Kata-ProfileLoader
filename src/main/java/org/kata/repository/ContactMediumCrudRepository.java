package org.kata.repository;

import org.kata.entity.ContactMedium;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactMediumCrudRepository extends CrudRepository<ContactMedium, String> {
}
