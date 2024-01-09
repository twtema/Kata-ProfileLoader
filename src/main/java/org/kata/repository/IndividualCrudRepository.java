package org.kata.repository;

import org.kata.entity.Individual;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IndividualCrudRepository extends CrudRepository<Individual, String> {
    Optional<Individual> findByIcp(String icp);
    @Query("SELECT c.individual FROM ContactMedium c WHERE c.value = :phone")
    Optional<Individual> findByPhone(@Param("phone") String phone);

}
