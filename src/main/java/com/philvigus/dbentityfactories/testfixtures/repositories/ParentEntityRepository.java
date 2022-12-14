package com.philvigus.dbentityfactories.testfixtures.repositories;

import com.philvigus.dbentityfactories.testfixtures.entities.ParentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The Parent entity repository used by tests to save the entity.
 */
public interface ParentEntityRepository extends JpaRepository<ParentEntity, Long> {
}
