package com.philvigus.dbentityfactories.testfixtures.hibernate.repositories;

import com.philvigus.dbentityfactories.testfixtures.entities.ParentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The Parent entity repository used by tests to save the entity.
 */
public interface ParentEntityHibernateRepository extends JpaRepository<ParentEntity, Long> {
}
