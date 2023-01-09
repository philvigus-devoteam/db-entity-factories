package com.philvigus.dbentityfactories.testfixtures.repositories;

import com.philvigus.dbentityfactories.testfixtures.entities.EntityWithUniqueAttributes;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The Basic entity with unique attributes repository used by tests to save the entity.
 */
public interface EntityWithUniqueAttributesRepository extends JpaRepository<EntityWithUniqueAttributes, Long> {
}
