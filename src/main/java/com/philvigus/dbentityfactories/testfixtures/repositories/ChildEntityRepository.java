package com.philvigus.dbentityfactories.testfixtures.repositories;

import com.philvigus.dbentityfactories.testfixtures.entities.ChildEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The Child entity repository used by tests to save the entity.
 */
public interface ChildEntityRepository extends JpaRepository<ChildEntity, Long> {
}
