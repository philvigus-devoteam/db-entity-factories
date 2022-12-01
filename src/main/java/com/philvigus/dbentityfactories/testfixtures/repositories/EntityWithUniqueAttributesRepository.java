package com.philvigus.dbentityfactories.testfixtures.repositories;

import com.philvigus.dbentityfactories.testfixtures.entities.EntityWithUniqueAttributes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntityWithUniqueAttributesRepository extends JpaRepository<EntityWithUniqueAttributes, Long> {
}
