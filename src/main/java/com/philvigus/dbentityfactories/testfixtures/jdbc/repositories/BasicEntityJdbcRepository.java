package com.philvigus.dbentityfactories.testfixtures.jdbc.repositories;

import com.philvigus.dbentityfactories.testfixtures.entities.BasicEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * The Basic entity repository used by tests to save the entity using JDBC.
 */
public interface BasicEntityJdbcRepository extends CrudRepository<BasicEntity, Long> {
}
