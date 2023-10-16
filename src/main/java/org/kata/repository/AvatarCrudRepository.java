package org.kata.repository;

import org.kata.entity.Avatar;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvatarCrudRepository extends CrudRepository<Avatar, String> {
}
