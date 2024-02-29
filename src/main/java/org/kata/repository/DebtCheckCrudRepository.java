package org.kata.repository;

import org.kata.entity.BlackListDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DebtCheckCrudRepository extends JpaRepository<BlackListDocument, String> {
}
