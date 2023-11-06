package org.kata.repository;

import org.kata.entity.Individual;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IndividualCrudRepository extends CrudRepository<Individual, String> {
    Optional<Individual> findByIcp(String icp);

    @Override
    void delete(Individual entity);

}
