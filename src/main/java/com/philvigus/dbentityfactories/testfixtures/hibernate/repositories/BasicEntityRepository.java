package com.philvigus.dbentityfactories.testfixtures.hibernate.repositories;

import com.philvigus.dbentityfactories.testfixtures.entities.BasicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The Basic entity repository used by tests to save the entity.
 */
public interface BasicEntityRepository extends JpaRepository<BasicEntity, Long> {
}
