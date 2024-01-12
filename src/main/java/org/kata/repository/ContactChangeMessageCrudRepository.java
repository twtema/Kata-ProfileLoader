package org.kata.repository;

import org.kata.entity.ContactChangeMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactChangeMessageCrudRepository extends JpaRepository<ContactChangeMessage, String> {
}
