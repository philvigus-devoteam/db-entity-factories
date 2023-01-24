package com.philvigus.dbentityfactories.testfixtures.factories;

import com.philvigus.dbentityfactories.annotations.EntityFactory;
import com.philvigus.dbentityfactories.factories.BaseEntityFactory;
import com.philvigus.dbentityfactories.testfixtures.entities.ParentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The Parent entity factory used by tests for this library.
 */
@EntityFactory
public class ParentEntityFactory extends BaseEntityFactory<ParentEntity> {
    /**
     * Instantiates a new Parent entity factory.
     *
     * @param repository the repository used to save instances of the entity
     */
    public ParentEntityFactory(final JpaRepository<ParentEntity, Long> repository) {
        super(ParentEntity.class, repository);
    }
}
