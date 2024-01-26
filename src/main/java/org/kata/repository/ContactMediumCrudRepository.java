package org.kata.repository;

import org.kata.entity.ContactMedium;
import org.kata.entity.enums.ContactMediumType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactMediumCrudRepository extends CrudRepository<ContactMedium, String> {

    Optional<ContactMedium> findByTypeAndValue(ContactMediumType type, String value);
}
