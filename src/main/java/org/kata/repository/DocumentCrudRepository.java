package org.kata.repository;

import org.kata.entity.Document;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentCrudRepository extends CrudRepository<Document, String> {
}
